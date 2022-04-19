import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrdine, Ordine } from '../ordine.model';

import { OrdineService } from './ordine.service';

describe('Ordine Service', () => {
  let service: OrdineService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrdine;
  let expectedResult: IOrdine | IOrdine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrdineService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a Ordine', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Ordine()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ordine', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ordine', () => {
      const patchObject = Object.assign({}, new Ordine());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ordine', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a Ordine', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrdineToCollectionIfMissing', () => {
      it('should add a Ordine to an empty array', () => {
        const ordine: IOrdine = { id: 123 };
        expectedResult = service.addOrdineToCollectionIfMissing([], ordine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordine);
      });

      it('should not add a Ordine to an array that contains it', () => {
        const ordine: IOrdine = { id: 123 };
        const ordineCollection: IOrdine[] = [
          {
            ...ordine,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrdineToCollectionIfMissing(ordineCollection, ordine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ordine to an array that doesn't contain it", () => {
        const ordine: IOrdine = { id: 123 };
        const ordineCollection: IOrdine[] = [{ id: 456 }];
        expectedResult = service.addOrdineToCollectionIfMissing(ordineCollection, ordine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordine);
      });

      it('should add only unique Ordine to an array', () => {
        const ordineArray: IOrdine[] = [{ id: 123 }, { id: 456 }, { id: 18432 }];
        const ordineCollection: IOrdine[] = [{ id: 123 }];
        expectedResult = service.addOrdineToCollectionIfMissing(ordineCollection, ...ordineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ordine: IOrdine = { id: 123 };
        const ordine2: IOrdine = { id: 456 };
        expectedResult = service.addOrdineToCollectionIfMissing([], ordine, ordine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordine);
        expect(expectedResult).toContain(ordine2);
      });

      it('should accept null and undefined values', () => {
        const ordine: IOrdine = { id: 123 };
        expectedResult = service.addOrdineToCollectionIfMissing([], null, ordine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordine);
      });

      it('should return initial array if no Ordine is added', () => {
        const ordineCollection: IOrdine[] = [{ id: 123 }];
        expectedResult = service.addOrdineToCollectionIfMissing(ordineCollection, undefined, null);
        expect(expectedResult).toEqual(ordineCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
