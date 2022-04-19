import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUtente, getUtenteIdentifier } from '../utente.model';

export type EntityResponseType = HttpResponse<IUtente>;
export type EntityArrayResponseType = HttpResponse<IUtente[]>;

@Injectable({ providedIn: 'root' })
export class UtenteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utentes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(utente: IUtente): Observable<EntityResponseType> {
    return this.http.post<IUtente>(this.resourceUrl, utente, { observe: 'response' });
  }

  update(utente: IUtente): Observable<EntityResponseType> {
    return this.http.put<IUtente>(`${this.resourceUrl}/${getUtenteIdentifier(utente) as number}`, utente, { observe: 'response' });
  }

  partialUpdate(utente: IUtente): Observable<EntityResponseType> {
    return this.http.patch<IUtente>(`${this.resourceUrl}/${getUtenteIdentifier(utente) as number}`, utente, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUtenteToCollectionIfMissing(utenteCollection: IUtente[], ...utentesToCheck: (IUtente | null | undefined)[]): IUtente[] {
    const utentes: IUtente[] = utentesToCheck.filter(isPresent);
    if (utentes.length > 0) {
      const utenteCollectionIdentifiers = utenteCollection.map(utenteItem => getUtenteIdentifier(utenteItem)!);
      const utentesToAdd = utentes.filter(utenteItem => {
        const utenteIdentifier = getUtenteIdentifier(utenteItem);
        if (utenteIdentifier == null || utenteCollectionIdentifiers.includes(utenteIdentifier)) {
          return false;
        }
        utenteCollectionIdentifiers.push(utenteIdentifier);
        return true;
      });
      return [...utentesToAdd, ...utenteCollection];
    }
    return utenteCollection;
  }
}
