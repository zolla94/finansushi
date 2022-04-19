import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'utente',
        data: { pageTitle: 'finanSushiApp.utente.home.title' },
        loadChildren: () => import('./utente/utente.module').then(m => m.UtenteModule),
      },
      {
        path: 'ordine',
        data: { pageTitle: 'finanSushiApp.ordine.home.title' },
        loadChildren: () => import('./ordine/ordine.module').then(m => m.OrdineModule),
      },
      {
        path: 'storico-ordini',
        data: { pageTitle: 'finanSushiApp.storicoOrdini.home.title' },
        loadChildren: () => import('./storico-ordini/storico-ordini.module').then(m => m.StoricoOrdiniModule),
      },
      {
        path: 'locale',
        data: { pageTitle: 'finanSushiApp.locale.home.title' },
        loadChildren: () => import('./locale/locale.module').then(m => m.LocaleModule),
      },
      {
        path: 'piatto',
        data: { pageTitle: 'finanSushiApp.piatto.home.title' },
        loadChildren: () => import('./piatto/piatto.module').then(m => m.PiattoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
