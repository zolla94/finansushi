import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IStoricoOrdini, StoricoOrdini } from '../storico-ordini.model';
import { StoricoOrdiniService } from '../service/storico-ordini.service';

import { StoricoOrdiniRoutingResolveService } from './storico-ordini-routing-resolve.service';

describe('StoricoOrdini routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: StoricoOrdiniRoutingResolveService;
  let service: StoricoOrdiniService;
  let resultStoricoOrdini: IStoricoOrdini | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(StoricoOrdiniRoutingResolveService);
    service = TestBed.inject(StoricoOrdiniService);
    resultStoricoOrdini = undefined;
  });

  describe('resolve', () => {
    it('should return IStoricoOrdini returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStoricoOrdini = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStoricoOrdini).toEqual({ id: 123 });
    });

    it('should return new IStoricoOrdini if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStoricoOrdini = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStoricoOrdini).toEqual(new StoricoOrdini());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as StoricoOrdini })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStoricoOrdini = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStoricoOrdini).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
