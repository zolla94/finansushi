import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrdineDetailComponent } from './ordine-detail.component';

describe('Ordine Management Detail Component', () => {
  let comp: OrdineDetailComponent;
  let fixture: ComponentFixture<OrdineDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrdineDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ordine: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OrdineDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrdineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ordine on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ordine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
