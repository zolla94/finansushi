import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StoricoOrdiniService } from '../service/storico-ordini.service';
import { IStoricoOrdini, StoricoOrdini } from '../storico-ordini.model';
import { IUtente } from 'app/entities/utente/utente.model';
import { UtenteService } from 'app/entities/utente/service/utente.service';
import { ILocale } from 'app/entities/locale/locale.model';
import { LocaleService } from 'app/entities/locale/service/locale.service';

import { StoricoOrdiniUpdateComponent } from './storico-ordini-update.component';

describe('StoricoOrdini Management Update Component', () => {
  let comp: StoricoOrdiniUpdateComponent;
  let fixture: ComponentFixture<StoricoOrdiniUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let storicoOrdiniService: StoricoOrdiniService;
  let utenteService: UtenteService;
  let localeService: LocaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StoricoOrdiniUpdateComponent],
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
      .overrideTemplate(StoricoOrdiniUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StoricoOrdiniUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    storicoOrdiniService = TestBed.inject(StoricoOrdiniService);
    utenteService = TestBed.inject(UtenteService);
    localeService = TestBed.inject(LocaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Utente query and add missing value', () => {
      const storicoOrdini: IStoricoOrdini = { id: 456 };
      const utente: IUtente = { id: 46933 };
      storicoOrdini.utente = utente;

      const utenteCollection: IUtente[] = [{ id: 6363 }];
      jest.spyOn(utenteService, 'query').mockReturnValue(of(new HttpResponse({ body: utenteCollection })));
      const additionalUtentes = [utente];
      const expectedCollection: IUtente[] = [...additionalUtentes, ...utenteCollection];
      jest.spyOn(utenteService, 'addUtenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ storicoOrdini });
      comp.ngOnInit();

      expect(utenteService.query).toHaveBeenCalled();
      expect(utenteService.addUtenteToCollectionIfMissing).toHaveBeenCalledWith(utenteCollection, ...additionalUtentes);
      expect(comp.utentesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Locale query and add missing value', () => {
      const storicoOrdini: IStoricoOrdini = { id: 456 };
      const locale: ILocale = { id: 13283 };
      storicoOrdini.locale = locale;

      const localeCollection: ILocale[] = [{ id: 45100 }];
      jest.spyOn(localeService, 'query').mockReturnValue(of(new HttpResponse({ body: localeCollection })));
      const additionalLocales = [locale];
      const expectedCollection: ILocale[] = [...additionalLocales, ...localeCollection];
      jest.spyOn(localeService, 'addLocaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ storicoOrdini });
      comp.ngOnInit();

      expect(localeService.query).toHaveBeenCalled();
      expect(localeService.addLocaleToCollectionIfMissing).toHaveBeenCalledWith(localeCollection, ...additionalLocales);
      expect(comp.localesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const storicoOrdini: IStoricoOrdini = { id: 456 };
      const utente: IUtente = { id: 3983 };
      storicoOrdini.utente = utente;
      const locale: ILocale = { id: 77249 };
      storicoOrdini.locale = locale;

      activatedRoute.data = of({ storicoOrdini });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(storicoOrdini));
      expect(comp.utentesSharedCollection).toContain(utente);
      expect(comp.localesSharedCollection).toContain(locale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StoricoOrdini>>();
      const storicoOrdini = { id: 123 };
      jest.spyOn(storicoOrdiniService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storicoOrdini });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: storicoOrdini }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(storicoOrdiniService.update).toHaveBeenCalledWith(storicoOrdini);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StoricoOrdini>>();
      const storicoOrdini = new StoricoOrdini();
      jest.spyOn(storicoOrdiniService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storicoOrdini });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: storicoOrdini }));
      saveSubject.complete();

      // THEN
      expect(storicoOrdiniService.create).toHaveBeenCalledWith(storicoOrdini);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StoricoOrdini>>();
      const storicoOrdini = { id: 123 };
      jest.spyOn(storicoOrdiniService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storicoOrdini });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(storicoOrdiniService.update).toHaveBeenCalledWith(storicoOrdini);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUtenteById', () => {
      it('Should return tracked Utente primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUtenteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLocaleById', () => {
      it('Should return tracked Locale primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLocaleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
