import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UtenteService } from '../service/utente.service';
import { IUtente, Utente } from '../utente.model';

import { UtenteUpdateComponent } from './utente-update.component';

describe('Utente Management Update Component', () => {
  let comp: UtenteUpdateComponent;
  let fixture: ComponentFixture<UtenteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utenteService: UtenteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [UtenteUpdateComponent],
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
      .overrideTemplate(UtenteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtenteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utenteService = TestBed.inject(UtenteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const utente: IUtente = { id: 456 };

      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(utente));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Utente>>();
      const utente = { id: 123 };
      jest.spyOn(utenteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utente }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(utenteService.update).toHaveBeenCalledWith(utente);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Utente>>();
      const utente = new Utente();
      jest.spyOn(utenteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utente }));
      saveSubject.complete();

      // THEN
      expect(utenteService.create).toHaveBeenCalledWith(utente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Utente>>();
      const utente = { id: 123 };
      jest.spyOn(utenteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utenteService.update).toHaveBeenCalledWith(utente);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
