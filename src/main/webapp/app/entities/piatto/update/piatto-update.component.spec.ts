import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PiattoService } from '../service/piatto.service';
import { IPiatto, Piatto } from '../piatto.model';
import { IOrdine } from 'app/entities/ordine/ordine.model';
import { OrdineService } from 'app/entities/ordine/service/ordine.service';
import { IStoricoOrdini } from 'app/entities/storico-ordini/storico-ordini.model';
import { StoricoOrdiniService } from 'app/entities/storico-ordini/service/storico-ordini.service';
import { ILocale } from 'app/entities/locale/locale.model';
import { LocaleService } from 'app/entities/locale/service/locale.service';

import { PiattoUpdateComponent } from './piatto-update.component';

describe('Piatto Management Update Component', () => {
  let comp: PiattoUpdateComponent;
  let fixture: ComponentFixture<PiattoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let piattoService: PiattoService;
  let ordineService: OrdineService;
  let storicoOrdiniService: StoricoOrdiniService;
  let localeService: LocaleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PiattoUpdateComponent],
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
      .overrideTemplate(PiattoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PiattoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    piattoService = TestBed.inject(PiattoService);
    ordineService = TestBed.inject(OrdineService);
    storicoOrdiniService = TestBed.inject(StoricoOrdiniService);
    localeService = TestBed.inject(LocaleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ordine query and add missing value', () => {
      const piatto: IPiatto = { id: 456 };
      const ordine: IOrdine = { id: 72534 };
      piatto.ordine = ordine;

      const ordineCollection: IOrdine[] = [{ id: 50513 }];
      jest.spyOn(ordineService, 'query').mockReturnValue(of(new HttpResponse({ body: ordineCollection })));
      const expectedCollection: IOrdine[] = [ordine, ...ordineCollection];
      jest.spyOn(ordineService, 'addOrdineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ piatto });
      comp.ngOnInit();

      expect(ordineService.query).toHaveBeenCalled();
      expect(ordineService.addOrdineToCollectionIfMissing).toHaveBeenCalledWith(ordineCollection, ordine);
      expect(comp.ordinesCollection).toEqual(expectedCollection);
    });

    it('Should call storicoOrdini query and add missing value', () => {
      const piatto: IPiatto = { id: 456 };
      const storicoOrdini: IStoricoOrdini = { id: 6396 };
      piatto.storicoOrdini = storicoOrdini;

      const storicoOrdiniCollection: IStoricoOrdini[] = [{ id: 79505 }];
      jest.spyOn(storicoOrdiniService, 'query').mockReturnValue(of(new HttpResponse({ body: storicoOrdiniCollection })));
      const expectedCollection: IStoricoOrdini[] = [storicoOrdini, ...storicoOrdiniCollection];
      jest.spyOn(storicoOrdiniService, 'addStoricoOrdiniToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ piatto });
      comp.ngOnInit();

      expect(storicoOrdiniService.query).toHaveBeenCalled();
      expect(storicoOrdiniService.addStoricoOrdiniToCollectionIfMissing).toHaveBeenCalledWith(storicoOrdiniCollection, storicoOrdini);
      expect(comp.storicoOrdinisCollection).toEqual(expectedCollection);
    });

    it('Should call Locale query and add missing value', () => {
      const piatto: IPiatto = { id: 456 };
      const locale: ILocale = { id: 10568 };
      piatto.locale = locale;

      const localeCollection: ILocale[] = [{ id: 28732 }];
      jest.spyOn(localeService, 'query').mockReturnValue(of(new HttpResponse({ body: localeCollection })));
      const additionalLocales = [locale];
      const expectedCollection: ILocale[] = [...additionalLocales, ...localeCollection];
      jest.spyOn(localeService, 'addLocaleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ piatto });
      comp.ngOnInit();

      expect(localeService.query).toHaveBeenCalled();
      expect(localeService.addLocaleToCollectionIfMissing).toHaveBeenCalledWith(localeCollection, ...additionalLocales);
      expect(comp.localesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const piatto: IPiatto = { id: 456 };
      const ordine: IOrdine = { id: 6295 };
      piatto.ordine = ordine;
      const storicoOrdini: IStoricoOrdini = { id: 70098 };
      piatto.storicoOrdini = storicoOrdini;
      const locale: ILocale = { id: 41004 };
      piatto.locale = locale;

      activatedRoute.data = of({ piatto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(piatto));
      expect(comp.ordinesCollection).toContain(ordine);
      expect(comp.storicoOrdinisCollection).toContain(storicoOrdini);
      expect(comp.localesSharedCollection).toContain(locale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Piatto>>();
      const piatto = { id: 123 };
      jest.spyOn(piattoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ piatto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: piatto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(piattoService.update).toHaveBeenCalledWith(piatto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Piatto>>();
      const piatto = new Piatto();
      jest.spyOn(piattoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ piatto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: piatto }));
      saveSubject.complete();

      // THEN
      expect(piattoService.create).toHaveBeenCalledWith(piatto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Piatto>>();
      const piatto = { id: 123 };
      jest.spyOn(piattoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ piatto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(piattoService.update).toHaveBeenCalledWith(piatto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOrdineById', () => {
      it('Should return tracked Ordine primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrdineById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackStoricoOrdiniById', () => {
      it('Should return tracked StoricoOrdini primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStoricoOrdiniById(0, entity);
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
