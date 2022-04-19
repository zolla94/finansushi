import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPiatto, getPiattoIdentifier } from '../piatto.model';

export type EntityResponseType = HttpResponse<IPiatto>;
export type EntityArrayResponseType = HttpResponse<IPiatto[]>;

@Injectable({ providedIn: 'root' })
export class PiattoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/piattos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(piatto: IPiatto): Observable<EntityResponseType> {
    return this.http.post<IPiatto>(this.resourceUrl, piatto, { observe: 'response' });
  }

  update(piatto: IPiatto): Observable<EntityResponseType> {
    return this.http.put<IPiatto>(`${this.resourceUrl}/${getPiattoIdentifier(piatto) as number}`, piatto, { observe: 'response' });
  }

  partialUpdate(piatto: IPiatto): Observable<EntityResponseType> {
    return this.http.patch<IPiatto>(`${this.resourceUrl}/${getPiattoIdentifier(piatto) as number}`, piatto, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPiatto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPiatto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPiattoToCollectionIfMissing(piattoCollection: IPiatto[], ...piattosToCheck: (IPiatto | null | undefined)[]): IPiatto[] {
    const piattos: IPiatto[] = piattosToCheck.filter(isPresent);
    if (piattos.length > 0) {
      const piattoCollectionIdentifiers = piattoCollection.map(piattoItem => getPiattoIdentifier(piattoItem)!);
      const piattosToAdd = piattos.filter(piattoItem => {
        const piattoIdentifier = getPiattoIdentifier(piattoItem);
        if (piattoIdentifier == null || piattoCollectionIdentifiers.includes(piattoIdentifier)) {
          return false;
        }
        piattoCollectionIdentifiers.push(piattoIdentifier);
        return true;
      });
      return [...piattosToAdd, ...piattoCollection];
    }
    return piattoCollection;
  }
}
