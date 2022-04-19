import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PiattoDetailComponent } from './piatto-detail.component';

describe('Piatto Management Detail Component', () => {
  let comp: PiattoDetailComponent;
  let fixture: ComponentFixture<PiattoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PiattoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ piatto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PiattoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PiattoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load piatto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.piatto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
