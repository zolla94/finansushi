import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrdineComponent } from './list/ordine.component';
import { OrdineDetailComponent } from './detail/ordine-detail.component';
import { OrdineUpdateComponent } from './update/ordine-update.component';
import { OrdineDeleteDialogComponent } from './delete/ordine-delete-dialog.component';
import { OrdineRoutingModule } from './route/ordine-routing.module';

@NgModule({
  imports: [SharedModule, OrdineRoutingModule],
  declarations: [OrdineComponent, OrdineDetailComponent, OrdineUpdateComponent, OrdineDeleteDialogComponent],
  entryComponents: [OrdineDeleteDialogComponent],
})
export class OrdineModule {}
