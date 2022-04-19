import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPiatto, Piatto } from '../piatto.model';
import { PiattoService } from '../service/piatto.service';
import { IOrdine } from 'app/entities/ordine/ordine.model';
import { OrdineService } from 'app/entities/ordine/service/ordine.service';
import { IStoricoOrdini } from 'app/entities/storico-ordini/storico-ordini.model';
import { StoricoOrdiniService } from 'app/entities/storico-ordini/service/storico-ordini.service';
import { ILocale } from 'app/entities/locale/locale.model';
import { LocaleService } from 'app/entities/locale/service/locale.service';

@Component({
  selector: 'jhi-piatto-update',
  templateUrl: './piatto-update.component.html',
})
export class PiattoUpdateComponent implements OnInit {
  isSaving = false;

  ordinesCollection: IOrdine[] = [];
  storicoOrdinisCollection: IStoricoOrdini[] = [];
  localesSharedCollection: ILocale[] = [];

  editForm = this.fb.group({
    id: [],
    codice: [],
    descrizione: [],
    url: [],
    spicy: [],
    vegan: [],
    limiteOrdine: [],
    ordine: [],
    storicoOrdini: [],
    locale: [],
  });

  constructor(
    protected piattoService: PiattoService,
    protected ordineService: OrdineService,
    protected storicoOrdiniService: StoricoOrdiniService,
    protected localeService: LocaleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ piatto }) => {
      this.updateForm(piatto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const piatto = this.createFromForm();
    if (piatto.id !== undefined) {
      this.subscribeToSaveResponse(this.piattoService.update(piatto));
    } else {
      this.subscribeToSaveResponse(this.piattoService.create(piatto));
    }
  }

  trackOrdineById(_index: number, item: IOrdine): number {
    return item.id!;
  }

  trackStoricoOrdiniById(_index: number, item: IStoricoOrdini): number {
    return item.id!;
  }

  trackLocaleById(_index: number, item: ILocale): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPiatto>>): void {
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

  protected updateForm(piatto: IPiatto): void {
    this.editForm.patchValue({
      id: piatto.id,
      codice: piatto.codice,
      descrizione: piatto.descrizione,
      url: piatto.url,
      spicy: piatto.spicy,
      vegan: piatto.vegan,
      limiteOrdine: piatto.limiteOrdine,
      ordine: piatto.ordine,
      storicoOrdini: piatto.storicoOrdini,
      locale: piatto.locale,
    });

    this.ordinesCollection = this.ordineService.addOrdineToCollectionIfMissing(this.ordinesCollection, piatto.ordine);
    this.storicoOrdinisCollection = this.storicoOrdiniService.addStoricoOrdiniToCollectionIfMissing(
      this.storicoOrdinisCollection,
      piatto.storicoOrdini
    );
    this.localesSharedCollection = this.localeService.addLocaleToCollectionIfMissing(this.localesSharedCollection, piatto.locale);
  }

  protected loadRelationshipsOptions(): void {
    this.ordineService
      .query({ filter: 'piatto-is-null' })
      .pipe(map((res: HttpResponse<IOrdine[]>) => res.body ?? []))
      .pipe(map((ordines: IOrdine[]) => this.ordineService.addOrdineToCollectionIfMissing(ordines, this.editForm.get('ordine')!.value)))
      .subscribe((ordines: IOrdine[]) => (this.ordinesCollection = ordines));

    this.storicoOrdiniService
      .query({ filter: 'piatto-is-null' })
      .pipe(map((res: HttpResponse<IStoricoOrdini[]>) => res.body ?? []))
      .pipe(
        map((storicoOrdinis: IStoricoOrdini[]) =>
          this.storicoOrdiniService.addStoricoOrdiniToCollectionIfMissing(storicoOrdinis, this.editForm.get('storicoOrdini')!.value)
        )
      )
      .subscribe((storicoOrdinis: IStoricoOrdini[]) => (this.storicoOrdinisCollection = storicoOrdinis));

    this.localeService
      .query()
      .pipe(map((res: HttpResponse<ILocale[]>) => res.body ?? []))
      .pipe(map((locales: ILocale[]) => this.localeService.addLocaleToCollectionIfMissing(locales, this.editForm.get('locale')!.value)))
      .subscribe((locales: ILocale[]) => (this.localesSharedCollection = locales));
  }

  protected createFromForm(): IPiatto {
    return {
      ...new Piatto(),
      id: this.editForm.get(['id'])!.value,
      codice: this.editForm.get(['codice'])!.value,
      descrizione: this.editForm.get(['descrizione'])!.value,
      url: this.editForm.get(['url'])!.value,
      spicy: this.editForm.get(['spicy'])!.value,
      vegan: this.editForm.get(['vegan'])!.value,
      limiteOrdine: this.editForm.get(['limiteOrdine'])!.value,
      ordine: this.editForm.get(['ordine'])!.value,
      storicoOrdini: this.editForm.get(['storicoOrdini'])!.value,
      locale: this.editForm.get(['locale'])!.value,
    };
  }
}
