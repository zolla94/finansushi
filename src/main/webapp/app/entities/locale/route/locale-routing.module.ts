import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocaleComponent } from '../list/locale.component';
import { LocaleDetailComponent } from '../detail/locale-detail.component';
import { LocaleUpdateComponent } from '../update/locale-update.component';
import { LocaleRoutingResolveService } from './locale-routing-resolve.service';

const localeRoute: Routes = [
  {
    path: '',
    component: LocaleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocaleDetailComponent,
    resolve: {
      locale: LocaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocaleUpdateComponent,
    resolve: {
      locale: LocaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocaleUpdateComponent,
    resolve: {
      locale: LocaleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(localeRoute)],
  exports: [RouterModule],
})
export class LocaleRoutingModule {}
