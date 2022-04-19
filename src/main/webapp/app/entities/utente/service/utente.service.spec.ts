import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUtente, Utente } from '../utente.model';

import { UtenteService } from './utente.service';

describe('Utente Service', () => {
  let service: UtenteService;
  let httpMock: HttpTestingController;
  let elemDefault: IUtente;
  let expectedResult: IUtente | IUtente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UtenteService);
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

    it('should create a Utente', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Utente()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Utente', () => {
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

    it('should partial update a Utente', () => {
      const patchObject = Object.assign({}, new Utente());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Utente', () => {
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

    it('should delete a Utente', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUtenteToCollectionIfMissing', () => {
      it('should add a Utente to an empty array', () => {
        const utente: IUtente = { id: 123 };
        expectedResult = service.addUtenteToCollectionIfMissing([], utente);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utente);
      });

      it('should not add a Utente to an array that contains it', () => {
        const utente: IUtente = { id: 123 };
        const utenteCollection: IUtente[] = [
          {
            ...utente,
          },
          { id: 456 },
        ];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, utente);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Utente to an array that doesn't contain it", () => {
        const utente: IUtente = { id: 123 };
        const utenteCollection: IUtente[] = [{ id: 456 }];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, utente);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utente);
      });

      it('should add only unique Utente to an array', () => {
        const utenteArray: IUtente[] = [{ id: 123 }, { id: 456 }, { id: 46789 }];
        const utenteCollection: IUtente[] = [{ id: 123 }];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, ...utenteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const utente: IUtente = { id: 123 };
        const utente2: IUtente = { id: 456 };
        expectedResult = service.addUtenteToCollectionIfMissing([], utente, utente2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(utente);
        expect(expectedResult).toContain(utente2);
      });

      it('should accept null and undefined values', () => {
        const utente: IUtente = { id: 123 };
        expectedResult = service.addUtenteToCollectionIfMissing([], null, utente, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(utente);
      });

      it('should return initial array if no Utente is added', () => {
        const utenteCollection: IUtente[] = [{ id: 123 }];
        expectedResult = service.addUtenteToCollectionIfMissing(utenteCollection, undefined, null);
        expect(expectedResult).toEqual(utenteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
