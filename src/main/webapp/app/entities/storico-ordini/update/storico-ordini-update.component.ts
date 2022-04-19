import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IStoricoOrdini, StoricoOrdini } from '../storico-ordini.model';
import { StoricoOrdiniService } from '../service/storico-ordini.service';
import { IUtente } from 'app/entities/utente/utente.model';
import { UtenteService } from 'app/entities/utente/service/utente.service';
import { ILocale } from 'app/entities/locale/locale.model';
import { LocaleService } from 'app/entities/locale/service/locale.service';

@Component({
  selector: 'jhi-storico-ordini-update',
  templateUrl: './storico-ordini-update.component.html',
})
export class StoricoOrdiniUpdateComponent implements OnInit {
  isSaving = false;

  utentesSharedCollection: IUtente[] = [];
  localesSharedCollection: ILocale[] = [];

  editForm = this.fb.group({
    id: [],
    note: [],
    utente: [],
    locale: [],
  });

  constructor(
    protected storicoOrdiniService: StoricoOrdiniService,
    protected utenteService: UtenteService,
    protected localeService: LocaleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storicoOrdini }) => {
      this.updateForm(storicoOrdini);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const storicoOrdini = this.createFromForm();
    if (storicoOrdini.id !== undefined) {
      this.subscribeToSaveResponse(this.storicoOrdiniService.update(storicoOrdini));
    } else {
      this.subscribeToSaveResponse(this.storicoOrdiniService.create(storicoOrdini));
    }
  }

  trackUtenteById(_index: number, item: IUtente): number {
    return item.id!;
  }

  trackLocaleById(_index: number, item: ILocale): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStoricoOrdini>>): void {
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

  protected updateForm(storicoOrdini: IStoricoOrdini): void {
    this.editForm.patchValue({
      id: storicoOrdini.id,
      note: storicoOrdini.note,
      utente: storicoOrdini.utente,
      locale: storicoOrdini.locale,
    });

    this.utentesSharedCollection = this.utenteService.addUtenteToCollectionIfMissing(this.utentesSharedCollection, storicoOrdini.utente);
    this.localesSharedCollection = this.localeService.addLocaleToCollectionIfMissing(this.localesSharedCollection, storicoOrdini.locale);
  }

  protected loadRelationshipsOptions(): void {
    this.utenteService
      .query()
      .pipe(map((res: HttpResponse<IUtente[]>) => res.body ?? []))
      .pipe(map((utentes: IUtente[]) => this.utenteService.addUtenteToCollectionIfMissing(utentes, this.editForm.get('utente')!.value)))
      .subscribe((utentes: IUtente[]) => (this.utentesSharedCollection = utentes));

    this.localeService
      .query()
      .pipe(map((res: HttpResponse<ILocale[]>) => res.body ?? []))
      .pipe(map((locales: ILocale[]) => this.localeService.addLocaleToCollectionIfMissing(locales, this.editForm.get('locale')!.value)))
      .subscribe((locales: ILocale[]) => (this.localesSharedCollection = locales));
  }

  protected createFromForm(): IStoricoOrdini {
    return {
      ...new StoricoOrdini(),
      id: this.editForm.get(['id'])!.value,
      note: this.editForm.get(['note'])!.value,
      utente: this.editForm.get(['utente'])!.value,
      locale: this.editForm.get(['locale'])!.value,
    };
  }
}
