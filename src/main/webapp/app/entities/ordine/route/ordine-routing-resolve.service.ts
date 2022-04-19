import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrdine, Ordine } from '../ordine.model';
import { OrdineService } from '../service/ordine.service';

@Injectable({ providedIn: 'root' })
export class OrdineRoutingResolveService implements Resolve<IOrdine> {
  constructor(protected service: OrdineService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrdine> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ordine: HttpResponse<Ordine>) => {
          if (ordine.body) {
            return of(ordine.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ordine());
  }
}
