import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrdine } from '../ordine.model';
import { OrdineService } from '../service/ordine.service';

@Component({
  templateUrl: './ordine-delete-dialog.component.html',
})
export class OrdineDeleteDialogComponent {
  ordine?: IOrdine;

  constructor(protected ordineService: OrdineService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordineService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
