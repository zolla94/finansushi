import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPiatto, Piatto } from '../piatto.model';

import { PiattoService } from './piatto.service';

describe('Piatto Service', () => {
  let service: PiattoService;
  let httpMock: HttpTestingController;
  let elemDefault: IPiatto;
  let expectedResult: IPiatto | IPiatto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PiattoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      codice: 'AAAAAAA',
      descrizione: 'AAAAAAA',
      url: 'AAAAAAA',
      spicy: false,
      vegan: false,
      limiteOrdine: false,
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

    it('should create a Piatto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Piatto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Piatto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          codice: 'BBBBBB',
          descrizione: 'BBBBBB',
          url: 'BBBBBB',
          spicy: true,
          vegan: true,
          limiteOrdine: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Piatto', () => {
      const patchObject = Object.assign(
        {
          codice: 'BBBBBB',
          descrizione: 'BBBBBB',
          url: 'BBBBBB',
          vegan: true,
          limiteOrdine: true,
        },
        new Piatto()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Piatto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          codice: 'BBBBBB',
          descrizione: 'BBBBBB',
          url: 'BBBBBB',
          spicy: true,
          vegan: true,
          limiteOrdine: true,
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

    it('should delete a Piatto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPiattoToCollectionIfMissing', () => {
      it('should add a Piatto to an empty array', () => {
        const piatto: IPiatto = { id: 123 };
        expectedResult = service.addPiattoToCollectionIfMissing([], piatto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(piatto);
      });

      it('should not add a Piatto to an array that contains it', () => {
        const piatto: IPiatto = { id: 123 };
        const piattoCollection: IPiatto[] = [
          {
            ...piatto,
          },
          { id: 456 },
        ];
        expectedResult = service.addPiattoToCollectionIfMissing(piattoCollection, piatto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Piatto to an array that doesn't contain it", () => {
        const piatto: IPiatto = { id: 123 };
        const piattoCollection: IPiatto[] = [{ id: 456 }];
        expectedResult = service.addPiattoToCollectionIfMissing(piattoCollection, piatto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(piatto);
      });

      it('should add only unique Piatto to an array', () => {
        const piattoArray: IPiatto[] = [{ id: 123 }, { id: 456 }, { id: 60039 }];
        const piattoCollection: IPiatto[] = [{ id: 123 }];
        expectedResult = service.addPiattoToCollectionIfMissing(piattoCollection, ...piattoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const piatto: IPiatto = { id: 123 };
        const piatto2: IPiatto = { id: 456 };
        expectedResult = service.addPiattoToCollectionIfMissing([], piatto, piatto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(piatto);
        expect(expectedResult).toContain(piatto2);
      });

      it('should accept null and undefined values', () => {
        const piatto: IPiatto = { id: 123 };
        expectedResult = service.addPiattoToCollectionIfMissing([], null, piatto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(piatto);
      });

      it('should return initial array if no Piatto is added', () => {
        const piattoCollection: IPiatto[] = [{ id: 123 }];
        expectedResult = service.addPiattoToCollectionIfMissing(piattoCollection, undefined, null);
        expect(expectedResult).toEqual(piattoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
