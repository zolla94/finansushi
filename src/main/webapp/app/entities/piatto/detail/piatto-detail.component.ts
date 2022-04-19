import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPiatto } from '../piatto.model';

@Component({
  selector: 'jhi-piatto-detail',
  templateUrl: './piatto-detail.component.html',
})
export class PiattoDetailComponent implements OnInit {
  piatto: IPiatto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ piatto }) => {
      this.piatto = piatto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
