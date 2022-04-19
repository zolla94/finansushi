import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStoricoOrdini, StoricoOrdini } from '../storico-ordini.model';
import { StoricoOrdiniService } from '../service/storico-ordini.service';

@Injectable({ providedIn: 'root' })
export class StoricoOrdiniRoutingResolveService implements Resolve<IStoricoOrdini> {
  constructor(protected service: StoricoOrdiniService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStoricoOrdini> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((storicoOrdini: HttpResponse<StoricoOrdini>) => {
          if (storicoOrdini.body) {
            return of(storicoOrdini.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StoricoOrdini());
  }
}
