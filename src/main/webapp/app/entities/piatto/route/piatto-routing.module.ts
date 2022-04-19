import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PiattoComponent } from '../list/piatto.component';
import { PiattoDetailComponent } from '../detail/piatto-detail.component';
import { PiattoUpdateComponent } from '../update/piatto-update.component';
import { PiattoRoutingResolveService } from './piatto-routing-resolve.service';

const piattoRoute: Routes = [
  {
    path: '',
    component: PiattoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PiattoDetailComponent,
    resolve: {
      piatto: PiattoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PiattoUpdateComponent,
    resolve: {
      piatto: PiattoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PiattoUpdateComponent,
    resolve: {
      piatto: PiattoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(piattoRoute)],
  exports: [RouterModule],
})
export class PiattoRoutingModule {}
