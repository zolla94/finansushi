import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocaleDetailComponent } from './locale-detail.component';

describe('Locale Management Detail Component', () => {
  let comp: LocaleDetailComponent;
  let fixture: ComponentFixture<LocaleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LocaleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ locale: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LocaleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LocaleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load locale on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.locale).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
