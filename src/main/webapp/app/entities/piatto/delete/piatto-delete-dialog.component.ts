import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPiatto } from '../piatto.model';
import { PiattoService } from '../service/piatto.service';

@Component({
  templateUrl: './piatto-delete-dialog.component.html',
})
export class PiattoDeleteDialogComponent {
  piatto?: IPiatto;

  constructor(protected piattoService: PiattoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.piattoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
