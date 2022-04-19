import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrdine } from '../ordine.model';

@Component({
  selector: 'jhi-ordine-detail',
  templateUrl: './ordine-detail.component.html',
})
export class OrdineDetailComponent implements OnInit {
  ordine: IOrdine | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordine }) => {
      this.ordine = ordine;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
