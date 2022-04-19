import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUtente, Utente } from '../utente.model';
import { UtenteService } from '../service/utente.service';

@Component({
  selector: 'jhi-utente-update',
  templateUrl: './utente-update.component.html',
})
export class UtenteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected utenteService: UtenteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utente }) => {
      this.updateForm(utente);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utente = this.createFromForm();
    if (utente.id !== undefined) {
      this.subscribeToSaveResponse(this.utenteService.update(utente));
    } else {
      this.subscribeToSaveResponse(this.utenteService.create(utente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(utente: IUtente): void {
    this.editForm.patchValue({
      id: utente.id,
    });
  }

  protected createFromForm(): IUtente {
    return {
      ...new Utente(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
