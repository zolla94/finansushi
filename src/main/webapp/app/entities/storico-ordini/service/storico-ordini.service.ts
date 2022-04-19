import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStoricoOrdini, getStoricoOrdiniIdentifier } from '../storico-ordini.model';

export type EntityResponseType = HttpResponse<IStoricoOrdini>;
export type EntityArrayResponseType = HttpResponse<IStoricoOrdini[]>;

@Injectable({ providedIn: 'root' })
export class StoricoOrdiniService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/storico-ordinis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(storicoOrdini: IStoricoOrdini): Observable<EntityResponseType> {
    return this.http.post<IStoricoOrdini>(this.resourceUrl, storicoOrdini, { observe: 'response' });
  }

  update(storicoOrdini: IStoricoOrdini): Observable<EntityResponseType> {
    return this.http.put<IStoricoOrdini>(`${this.resourceUrl}/${getStoricoOrdiniIdentifier(storicoOrdini) as number}`, storicoOrdini, {
      observe: 'response',
    });
  }

  partialUpdate(storicoOrdini: IStoricoOrdini): Observable<EntityResponseType> {
    return this.http.patch<IStoricoOrdini>(`${this.resourceUrl}/${getStoricoOrdiniIdentifier(storicoOrdini) as number}`, storicoOrdini, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStoricoOrdini>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStoricoOrdini[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStoricoOrdiniToCollectionIfMissing(
    storicoOrdiniCollection: IStoricoOrdini[],
    ...storicoOrdinisToCheck: (IStoricoOrdini | null | undefined)[]
  ): IStoricoOrdini[] {
    const storicoOrdinis: IStoricoOrdini[] = storicoOrdinisToCheck.filter(isPresent);
    if (storicoOrdinis.length > 0) {
      const storicoOrdiniCollectionIdentifiers = storicoOrdiniCollection.map(
        storicoOrdiniItem => getStoricoOrdiniIdentifier(storicoOrdiniItem)!
      );
      const storicoOrdinisToAdd = storicoOrdinis.filter(storicoOrdiniItem => {
        const storicoOrdiniIdentifier = getStoricoOrdiniIdentifier(storicoOrdiniItem);
        if (storicoOrdiniIdentifier == null || storicoOrdiniCollectionIdentifiers.includes(storicoOrdiniIdentifier)) {
          return false;
        }
        storicoOrdiniCollectionIdentifiers.push(storicoOrdiniIdentifier);
        return true;
      });
      return [...storicoOrdinisToAdd, ...storicoOrdiniCollection];
    }
    return storicoOrdiniCollection;
  }
}
