import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrdine, Ordine } from '../ordine.model';
import { OrdineService } from '../service/ordine.service';
import { ILocale } from 'app/entities/locale/locale.model';
import { LocaleService } from 'app/entities/locale/service/locale.service';
import { IUtente } from 'app/entities/utente/utente.model';
import { UtenteService } from 'app/entities/utente/service/utente.service';

@Component({
  selector: 'jhi-ordine-update',
  templateUrl: './ordine-update.component.html',
})
export class OrdineUpdateComponent implements OnInit {
  isSaving = false;

  localesSharedCollection: ILocale[] = [];
  utentesSharedCollection: IUtente[] = [];

  editForm = this.fb.group({
    id: [],
    locale: [],
    utente: [],
  });

  constructor(
    protected ordineService: OrdineService,
    protected localeService: LocaleService,
    protected utenteService: UtenteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordine }) => {
      this.updateForm(ordine);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordine = this.createFromForm();
    if (ordine.id !== undefined) {
      this.subscribeToSaveResponse(this.ordineService.update(ordine));
    } else {
      this.subscribeToSaveResponse(this.ordineService.create(ordine));
    }
  }

  trackLocaleById(_index: number, item: ILocale): number {
    return item.id!;
  }

  trackUtenteById(_index: number, item: IUtente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdine>>): void {
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

  protected updateForm(ordine: IOrdine): void {
    this.editForm.patchValue({
      id: ordine.id,
      locale: ordine.locale,
      utente: ordine.utente,
    });

    this.localesSharedCollection = this.localeService.addLocaleToCollectionIfMissing(this.localesSharedCollection, ordine.locale);
    this.utentesSharedCollection = this.utenteService.addUtenteToCollectionIfMissing(this.utentesSharedCollection, ordine.utente);
  }

  protected loadRelationshipsOptions(): void {
    this.localeService
      .query()
      .pipe(map((res: HttpResponse<ILocale[]>) => res.body ?? []))
      .pipe(map((locales: ILocale[]) => this.localeService.addLocaleToCollectionIfMissing(locales, this.editForm.get('locale')!.value)))
      .subscribe((locales: ILocale[]) => (this.localesSharedCollection = locales));

    this.utenteService
      .query()
      .pipe(map((res: HttpResponse<IUtente[]>) => res.body ?? []))
      .pipe(map((utentes: IUtente[]) => this.utenteService.addUtenteToCollectionIfMissing(utentes, this.editForm.get('utente')!.value)))
      .subscribe((utentes: IUtente[]) => (this.utentesSharedCollection = utentes));
  }

  protected createFromForm(): IOrdine {
    return {
      ...new Ordine(),
      id: this.editForm.get(['id'])!.value,
      locale: this.editForm.get(['locale'])!.value,
      utente: this.editForm.get(['utente'])!.value,
    };
  }
}
