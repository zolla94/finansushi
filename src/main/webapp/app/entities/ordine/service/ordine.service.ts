import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrdine, getOrdineIdentifier } from '../ordine.model';

export type EntityResponseType = HttpResponse<IOrdine>;
export type EntityArrayResponseType = HttpResponse<IOrdine[]>;

@Injectable({ providedIn: 'root' })
export class OrdineService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ordines');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ordine: IOrdine): Observable<EntityResponseType> {
    return this.http.post<IOrdine>(this.resourceUrl, ordine, { observe: 'response' });
  }

  update(ordine: IOrdine): Observable<EntityResponseType> {
    return this.http.put<IOrdine>(`${this.resourceUrl}/${getOrdineIdentifier(ordine) as number}`, ordine, { observe: 'response' });
  }

  partialUpdate(ordine: IOrdine): Observable<EntityResponseType> {
    return this.http.patch<IOrdine>(`${this.resourceUrl}/${getOrdineIdentifier(ordine) as number}`, ordine, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrdine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrdineToCollectionIfMissing(ordineCollection: IOrdine[], ...ordinesToCheck: (IOrdine | null | undefined)[]): IOrdine[] {
    const ordines: IOrdine[] = ordinesToCheck.filter(isPresent);
    if (ordines.length > 0) {
      const ordineCollectionIdentifiers = ordineCollection.map(ordineItem => getOrdineIdentifier(ordineItem)!);
      const ordinesToAdd = ordines.filter(ordineItem => {
        const ordineIdentifier = getOrdineIdentifier(ordineItem);
        if (ordineIdentifier == null || ordineCollectionIdentifiers.includes(ordineIdentifier)) {
          return false;
        }
        ordineCollectionIdentifiers.push(ordineIdentifier);
        return true;
      });
      return [...ordinesToAdd, ...ordineCollection];
    }
    return ordineCollection;
  }
}
