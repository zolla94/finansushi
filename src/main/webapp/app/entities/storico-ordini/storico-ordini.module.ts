import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StoricoOrdiniComponent } from './list/storico-ordini.component';
import { StoricoOrdiniDetailComponent } from './detail/storico-ordini-detail.component';
import { StoricoOrdiniUpdateComponent } from './update/storico-ordini-update.component';
import { StoricoOrdiniDeleteDialogComponent } from './delete/storico-ordini-delete-dialog.component';
import { StoricoOrdiniRoutingModule } from './route/storico-ordini-routing.module';

@NgModule({
  imports: [SharedModule, StoricoOrdiniRoutingModule],
  declarations: [StoricoOrdiniComponent, StoricoOrdiniDetailComponent, StoricoOrdiniUpdateComponent, StoricoOrdiniDeleteDialogComponent],
  entryComponents: [StoricoOrdiniDeleteDialogComponent],
})
export class StoricoOrdiniModule {}
