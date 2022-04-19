import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrdineService } from '../service/ordine.service';
import { IOrdine, Ordine } from '../ordine.model';
import { ILocale } from 'app/entities/locale/locale.model';
import { LocaleService } from 'app/entities/locale/service/locale.service';
import { IUtente } from 'app/entities/utente/utente.model';
import { UtenteService } from 'app/entities/utente/service/utente.service';

import { OrdineUpdateComponent } from './ordine-update.component';

describe('Ordine Management Update Component', () => {
  let comp: OrdineUpdateComponent;
  let fixture: ComponentFixture<OrdineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordineService: OrdineService;
  let localeService: LocaleService;
  let utenteService: UtenteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OrdineUpdateComponent],
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
      .overrideTemplate(OrdineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordineService = TestBed.inject(OrdineService);
    localeService = TestBed.inject(LocaleService);
    utenteService = TestBed.inject(UtenteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Locale query and add missing value', () => {
      const ordine: IOrdine = { id: 456 };
      const locale: ILocale = { id: 60669 };
      ordine.locale = locale;

      const localeCollection: ILocale[] = [{ id: 32276 }];
      jest.spyOn(localeService, 'query').mockReturnValue(of(new HttpResponse({ body: localeCollection })));
      const additionalLocales = [locale];
      const expectedCollection: ILocale[] = [...additionalLocales, ...localeCollection];
      jest.spyOn(localeService, 'addLocaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ordine });
      comp.ngOnInit();

      expect(localeService.query).toHaveBeenCalled();
      expect(localeService.addLocaleToCollectionIfMissing).toHaveBeenCalledWith(localeCollection, ...additionalLocales);
      expect(comp.localesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Utente query and add missing value', () => {
      const ordine: IOrdine = { id: 456 };
      const utente: IUtente = { id: 29382 };
      ordine.utente = utente;

      const utenteCollection: IUtente[] = [{ id: 30749 }];
      jest.spyOn(utenteService, 'query').mockReturnValue(of(new HttpResponse({ body: utenteCollection })));
      const additionalUtentes = [utente];
      const expectedCollection: IUtente[] = [...additionalUtentes, ...utenteCollection];
      jest.spyOn(utenteService, 'addUtenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ordine });
      comp.ngOnInit();

      expect(utenteService.query).toHaveBeenCalled();
      expect(utenteService.addUtenteToCollectionIfMissing).toHaveBeenCalledWith(utenteCollection, ...additionalUtentes);
      expect(comp.utentesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ordine: IOrdine = { id: 456 };
      const locale: ILocale = { id: 48719 };
      ordine.locale = locale;
      const utente: IUtente = { id: 46704 };
      ordine.utente = utente;

      activatedRoute.data = of({ ordine });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ordine));
      expect(comp.localesSharedCollection).toContain(locale);
      expect(comp.utentesSharedCollection).toContain(utente);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ordine>>();
      const ordine = { id: 123 };
      jest.spyOn(ordineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordine }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordineService.update).toHaveBeenCalledWith(ordine);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ordine>>();
      const ordine = new Ordine();
      jest.spyOn(ordineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordine }));
      saveSubject.complete();

      // THEN
      expect(ordineService.create).toHaveBeenCalledWith(ordine);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ordine>>();
      const ordine = { id: 123 };
      jest.spyOn(ordineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordineService.update).toHaveBeenCalledWith(ordine);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLocaleById', () => {
      it('Should return tracked Locale primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLocaleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUtenteById', () => {
      it('Should return tracked Utente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUtenteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
