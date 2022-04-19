import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILocale, Locale } from '../locale.model';
import { LocaleService } from '../service/locale.service';

@Component({
  selector: 'jhi-locale-update',
  templateUrl: './locale-update.component.html',
})
export class LocaleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
  });

  constructor(protected localeService: LocaleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locale }) => {
      this.updateForm(locale);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locale = this.createFromForm();
    if (locale.id !== undefined) {
      this.subscribeToSaveResponse(this.localeService.update(locale));
    } else {
      this.subscribeToSaveResponse(this.localeService.create(locale));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocale>>): void {
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

  protected updateForm(locale: ILocale): void {
    this.editForm.patchValue({
      id: locale.id,
      nome: locale.nome,
    });
  }

  protected createFromForm(): ILocale {
    return {
      ...new Locale(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
    };
  }
}
