import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUtente } from '../utente.model';

@Component({
  selector: 'jhi-utente-detail',
  templateUrl: './utente-detail.component.html',
})
export class UtenteDetailComponent implements OnInit {
  utente: IUtente | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utente }) => {
      this.utente = utente;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
