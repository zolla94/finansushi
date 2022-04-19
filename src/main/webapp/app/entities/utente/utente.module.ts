import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UtenteComponent } from './list/utente.component';
import { UtenteDetailComponent } from './detail/utente-detail.component';
import { UtenteUpdateComponent } from './update/utente-update.component';
import { UtenteDeleteDialogComponent } from './delete/utente-delete-dialog.component';
import { UtenteRoutingModule } from './route/utente-routing.module';

@NgModule({
  imports: [SharedModule, UtenteRoutingModule],
  declarations: [UtenteComponent, UtenteDetailComponent, UtenteUpdateComponent, UtenteDeleteDialogComponent],
  entryComponents: [UtenteDeleteDialogComponent],
})
export class UtenteModule {}
