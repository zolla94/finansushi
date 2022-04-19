import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocale } from '../locale.model';

@Component({
  selector: 'jhi-locale-detail',
  templateUrl: './locale-detail.component.html',
})
export class LocaleDetailComponent implements OnInit {
  locale: ILocale | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locale }) => {
      this.locale = locale;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
