import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LocaleService } from '../service/locale.service';
import { ILocale, Locale } from '../locale.model';

import { LocaleUpdateComponent } from './locale-update.component';

describe('Locale Management Update Component', () => {
  let comp: LocaleUpdateComponent;
  let fixture: ComponentFixture<LocaleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localeService: LocaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LocaleUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LocaleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocaleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localeService = TestBed.inject(LocaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const locale: ILocale = { id: 456 };

      activatedRoute.data = of({ locale });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(locale));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Locale>>();
      const locale = { id: 123 };
      jest.spyOn(localeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locale }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(localeService.update).toHaveBeenCalledWith(locale);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Locale>>();
      const locale = new Locale();
      jest.spyOn(localeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: locale }));
      saveSubject.complete();

      // THEN
      expect(localeService.create).toHaveBeenCalledWith(locale);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Locale>>();
      const locale = { id: 123 };
      jest.spyOn(localeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ locale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localeService.update).toHaveBeenCalledWith(locale);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
