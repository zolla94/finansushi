import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LocaleComponent } from './list/locale.component';
import { LocaleDetailComponent } from './detail/locale-detail.component';
import { LocaleUpdateComponent } from './update/locale-update.component';
import { LocaleDeleteDialogComponent } from './delete/locale-delete-dialog.component';
import { LocaleRoutingModule } from './route/locale-routing.module';

@NgModule({
  imports: [SharedModule, LocaleRoutingModule],
  declarations: [LocaleComponent, LocaleDetailComponent, LocaleUpdateComponent, LocaleDeleteDialogComponent],
  entryComponents: [LocaleDeleteDialogComponent],
})
export class LocaleModule {}
