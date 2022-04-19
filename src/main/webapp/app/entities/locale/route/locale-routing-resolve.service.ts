import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocale, Locale } from '../locale.model';
import { LocaleService } from '../service/locale.service';

@Injectable({ providedIn: 'root' })
export class LocaleRoutingResolveService implements Resolve<ILocale> {
  constructor(protected service: LocaleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocale> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((locale: HttpResponse<Locale>) => {
          if (locale.body) {
            return of(locale.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Locale());
  }
}
