import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtente, Utente } from '../utente.model';
import { UtenteService } from '../service/utente.service';

@Injectable({ providedIn: 'root' })
export class UtenteRoutingResolveService implements Resolve<IUtente> {
  constructor(protected service: UtenteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUtente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((utente: HttpResponse<Utente>) => {
          if (utente.body) {
            return of(utente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Utente());
  }
}
