import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StoricoOrdiniDetailComponent } from './storico-ordini-detail.component';

describe('StoricoOrdini Management Detail Component', () => {
  let comp: StoricoOrdiniDetailComponent;
  let fixture: ComponentFixture<StoricoOrdiniDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StoricoOrdiniDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ storicoOrdini: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StoricoOrdiniDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StoricoOrdiniDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load storicoOrdini on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.storicoOrdini).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
