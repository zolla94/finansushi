import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocale, Locale } from '../locale.model';

import { LocaleService } from './locale.service';

describe('Locale Service', () => {
  let service: LocaleService;
  let httpMock: HttpTestingController;
  let elemDefault: ILocale;
  let expectedResult: ILocale | ILocale[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LocaleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nome: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Locale', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Locale()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Locale', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Locale', () => {
      const patchObject = Object.assign(
        {
          nome: 'BBBBBB',
        },
        new Locale()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Locale', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nome: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Locale', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLocaleToCollectionIfMissing', () => {
      it('should add a Locale to an empty array', () => {
        const locale: ILocale = { id: 123 };
        expectedResult = service.addLocaleToCollectionIfMissing([], locale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locale);
      });

      it('should not add a Locale to an array that contains it', () => {
        const locale: ILocale = { id: 123 };
        const localeCollection: ILocale[] = [
          {
            ...locale,
          },
          { id: 456 },
        ];
        expectedResult = service.addLocaleToCollectionIfMissing(localeCollection, locale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Locale to an array that doesn't contain it", () => {
        const locale: ILocale = { id: 123 };
        const localeCollection: ILocale[] = [{ id: 456 }];
        expectedResult = service.addLocaleToCollectionIfMissing(localeCollection, locale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locale);
      });

      it('should add only unique Locale to an array', () => {
        const localeArray: ILocale[] = [{ id: 123 }, { id: 456 }, { id: 22141 }];
        const localeCollection: ILocale[] = [{ id: 123 }];
        expectedResult = service.addLocaleToCollectionIfMissing(localeCollection, ...localeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const locale: ILocale = { id: 123 };
        const locale2: ILocale = { id: 456 };
        expectedResult = service.addLocaleToCollectionIfMissing([], locale, locale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(locale);
        expect(expectedResult).toContain(locale2);
      });

      it('should accept null and undefined values', () => {
        const locale: ILocale = { id: 123 };
        expectedResult = service.addLocaleToCollectionIfMissing([], null, locale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(locale);
      });

      it('should return initial array if no Locale is added', () => {
        const localeCollection: ILocale[] = [{ id: 123 }];
        expectedResult = service.addLocaleToCollectionIfMissing(localeCollection, undefined, null);
        expect(expectedResult).toEqual(localeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
