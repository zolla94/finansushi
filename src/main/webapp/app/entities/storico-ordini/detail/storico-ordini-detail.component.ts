import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStoricoOrdini } from '../storico-ordini.model';

@Component({
  selector: 'jhi-storico-ordini-detail',
  templateUrl: './storico-ordini-detail.component.html',
})
export class StoricoOrdiniDetailComponent implements OnInit {
  storicoOrdini: IStoricoOrdini | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storicoOrdini }) => {
      this.storicoOrdini = storicoOrdini;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
