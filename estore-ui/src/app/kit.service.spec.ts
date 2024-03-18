import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { KitService } from './kits.service';


describe('KitService', () => {
  let service: KitService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [KitService]
    });
    service = TestBed.inject(KitService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return expected kits', () => {
    const expectedKits = [
      { id: 1, name: 'Mocktail', price: 5.99, quantity: 20 },
      { id: 2, name: 'Sangria', price: 7.99, quantity: 15 }
    ];

    service.getKits().subscribe(
      kits => expect(kits).toEqual(expectedKits, 'should return expected kits'),
      fail
    );

    const req = httpTestingController.expectOne('api/kits');
    expect(req.request.method).toEqual('GET');

    req.flush(expectedKits);
  });
});
