import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UtenteDetailComponent } from './utente-detail.component';

describe('Utente Management Detail Component', () => {
  let comp: UtenteDetailComponent;
  let fixture: ComponentFixture<UtenteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UtenteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ utente: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(UtenteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(UtenteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load utente on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.utente).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
