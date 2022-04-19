import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocale } from '../locale.model';
import { LocaleService } from '../service/locale.service';

@Component({
  templateUrl: './locale-delete-dialog.component.html',
})
export class LocaleDeleteDialogComponent {
  locale?: ILocale;

  constructor(protected localeService: LocaleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
