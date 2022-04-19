import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StoricoOrdiniComponent } from '../list/storico-ordini.component';
import { StoricoOrdiniDetailComponent } from '../detail/storico-ordini-detail.component';
import { StoricoOrdiniUpdateComponent } from '../update/storico-ordini-update.component';
import { StoricoOrdiniRoutingResolveService } from './storico-ordini-routing-resolve.service';

const storicoOrdiniRoute: Routes = [
  {
    path: '',
    component: StoricoOrdiniComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StoricoOrdiniDetailComponent,
    resolve: {
      storicoOrdini: StoricoOrdiniRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StoricoOrdiniUpdateComponent,
    resolve: {
      storicoOrdini: StoricoOrdiniRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StoricoOrdiniUpdateComponent,
    resolve: {
      storicoOrdini: StoricoOrdiniRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(storicoOrdiniRoute)],
  exports: [RouterModule],
})
export class StoricoOrdiniRoutingModule {}
