import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPiatto, Piatto } from '../piatto.model';
import { PiattoService } from '../service/piatto.service';

@Injectable({ providedIn: 'root' })
export class PiattoRoutingResolveService implements Resolve<IPiatto> {
  constructor(protected service: PiattoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPiatto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((piatto: HttpResponse<Piatto>) => {
          if (piatto.body) {
            return of(piatto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Piatto());
  }
}
