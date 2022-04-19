import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocale, getLocaleIdentifier } from '../locale.model';

export type EntityResponseType = HttpResponse<ILocale>;
export type EntityArrayResponseType = HttpResponse<ILocale[]>;

@Injectable({ providedIn: 'root' })
export class LocaleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/locales');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(locale: ILocale): Observable<EntityResponseType> {
    return this.http.post<ILocale>(this.resourceUrl, locale, { observe: 'response' });
  }

  update(locale: ILocale): Observable<EntityResponseType> {
    return this.http.put<ILocale>(`${this.resourceUrl}/${getLocaleIdentifier(locale) as number}`, locale, { observe: 'response' });
  }

  partialUpdate(locale: ILocale): Observable<EntityResponseType> {
    return this.http.patch<ILocale>(`${this.resourceUrl}/${getLocaleIdentifier(locale) as number}`, locale, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocale>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocale[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocaleToCollectionIfMissing(localeCollection: ILocale[], ...localesToCheck: (ILocale | null | undefined)[]): ILocale[] {
    const locales: ILocale[] = localesToCheck.filter(isPresent);
    if (locales.length > 0) {
      const localeCollectionIdentifiers = localeCollection.map(localeItem => getLocaleIdentifier(localeItem)!);
      const localesToAdd = locales.filter(localeItem => {
        const localeIdentifier = getLocaleIdentifier(localeItem);
        if (localeIdentifier == null || localeCollectionIdentifiers.includes(localeIdentifier)) {
          return false;
        }
        localeCollectionIdentifiers.push(localeIdentifier);
        return true;
      });
      return [...localesToAdd, ...localeCollection];
    }
    return localeCollection;
  }
}
