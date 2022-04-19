import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStoricoOrdini } from '../storico-ordini.model';
import { StoricoOrdiniService } from '../service/storico-ordini.service';

@Component({
  templateUrl: './storico-ordini-delete-dialog.component.html',
})
export class StoricoOrdiniDeleteDialogComponent {
  storicoOrdini?: IStoricoOrdini;

  constructor(protected storicoOrdiniService: StoricoOrdiniService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.storicoOrdiniService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
