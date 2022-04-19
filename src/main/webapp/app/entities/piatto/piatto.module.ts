import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PiattoComponent } from './list/piatto.component';
import { PiattoDetailComponent } from './detail/piatto-detail.component';
import { PiattoUpdateComponent } from './update/piatto-update.component';
import { PiattoDeleteDialogComponent } from './delete/piatto-delete-dialog.component';
import { PiattoRoutingModule } from './route/piatto-routing.module';

@NgModule({
  imports: [SharedModule, PiattoRoutingModule],
  declarations: [PiattoComponent, PiattoDetailComponent, PiattoUpdateComponent, PiattoDeleteDialogComponent],
  entryComponents: [PiattoDeleteDialogComponent],
})
export class PiattoModule {}
