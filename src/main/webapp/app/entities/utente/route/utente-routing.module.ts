import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UtenteComponent } from '../list/utente.component';
import { UtenteDetailComponent } from '../detail/utente-detail.component';
import { UtenteUpdateComponent } from '../update/utente-update.component';
import { UtenteRoutingResolveService } from './utente-routing-resolve.service';

const utenteRoute: Routes = [
  {
    path: '',
    component: UtenteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtenteDetailComponent,
    resolve: {
      utente: UtenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtenteUpdateComponent,
    resolve: {
      utente: UtenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtenteUpdateComponent,
    resolve: {
      utente: UtenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(utenteRoute)],
  exports: [RouterModule],
})
export class UtenteRoutingModule {}
