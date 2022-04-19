import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStoricoOrdini, StoricoOrdini } from '../storico-ordini.model';

import { StoricoOrdiniService } from './storico-ordini.service';

describe('StoricoOrdini Service', () => {
  let service: StoricoOrdiniService;
  let httpMock: HttpTestingController;
  let elemDefault: IStoricoOrdini;
  let expectedResult: IStoricoOrdini | IStoricoOrdini[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StoricoOrdiniService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      note: 'AAAAAAA',
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

    it('should create a StoricoOrdini', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new StoricoOrdini()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StoricoOrdini', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          note: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StoricoOrdini', () => {
      const patchObject = Object.assign(
        {
          note: 'BBBBBB',
        },
        new StoricoOrdini()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StoricoOrdini', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          note: 'BBBBBB',
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

    it('should delete a StoricoOrdini', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStoricoOrdiniToCollectionIfMissing', () => {
      it('should add a StoricoOrdini to an empty array', () => {
        const storicoOrdini: IStoricoOrdini = { id: 123 };
        expectedResult = service.addStoricoOrdiniToCollectionIfMissing([], storicoOrdini);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(storicoOrdini);
      });

      it('should not add a StoricoOrdini to an array that contains it', () => {
        const storicoOrdini: IStoricoOrdini = { id: 123 };
        const storicoOrdiniCollection: IStoricoOrdini[] = [
          {
            ...storicoOrdini,
          },
          { id: 456 },
        ];
        expectedResult = service.addStoricoOrdiniToCollectionIfMissing(storicoOrdiniCollection, storicoOrdini);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StoricoOrdini to an array that doesn't contain it", () => {
        const storicoOrdini: IStoricoOrdini = { id: 123 };
        const storicoOrdiniCollection: IStoricoOrdini[] = [{ id: 456 }];
        expectedResult = service.addStoricoOrdiniToCollectionIfMissing(storicoOrdiniCollection, storicoOrdini);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(storicoOrdini);
      });

      it('should add only unique StoricoOrdini to an array', () => {
        const storicoOrdiniArray: IStoricoOrdini[] = [{ id: 123 }, { id: 456 }, { id: 18841 }];
        const storicoOrdiniCollection: IStoricoOrdini[] = [{ id: 123 }];
        expectedResult = service.addStoricoOrdiniToCollectionIfMissing(storicoOrdiniCollection, ...storicoOrdiniArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const storicoOrdini: IStoricoOrdini = { id: 123 };
        const storicoOrdini2: IStoricoOrdini = { id: 456 };
        expectedResult = service.addStoricoOrdiniToCollectionIfMissing([], storicoOrdini, storicoOrdini2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(storicoOrdini);
        expect(expectedResult).toContain(storicoOrdini2);
      });

      it('should accept null and undefined values', () => {
        const storicoOrdini: IStoricoOrdini = { id: 123 };
        expectedResult = service.addStoricoOrdiniToCollectionIfMissing([], null, storicoOrdini, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(storicoOrdini);
      });

      it('should return initial array if no StoricoOrdini is added', () => {
        const storicoOrdiniCollection: IStoricoOrdini[] = [{ id: 123 }];
        expectedResult = service.addStoricoOrdiniToCollectionIfMissing(storicoOrdiniCollection, undefined, null);
        expect(expectedResult).toEqual(storicoOrdiniCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
