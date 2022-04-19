import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUtente } from '../utente.model';
import { UtenteService } from '../service/utente.service';

@Component({
  templateUrl: './utente-delete-dialog.component.html',
})
export class UtenteDeleteDialogComponent {
  utente?: IUtente;

  constructor(protected utenteService: UtenteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utenteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
