import { TestBed } from '@angular/core/testing';
import { ApiService } from './api.service';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

describe('ApiService - Gestión de Compras', () => {
  let service: ApiService;
  let httpSpy: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
    TestBed.configureTestingModule({
      providers: [
        ApiService,
        { provide: HttpClient, useValue: spy }
      ]
    });
    service = TestBed.inject(ApiService);
    httpSpy = TestBed.inject(HttpClient) as jasmine.SpyObj<HttpClient>;
  });

  it('debería obtener la lista de compras', (done) => {
    const mockCompras = [{ id: 1, producto: 'Manzana', cantidad: 10 }];
    httpSpy.get.and.returnValue(of(mockCompras));
    service.getVentas().subscribe(compras => {
      expect(compras.length).toBe(1);
      expect(compras[0].producto).toBe('Manzana');
      done();
    });
  });

  it('debería registrar una compra', (done) => {
    const compra = { producto: 'Manzana', cantidad: 5 };
    const mockResponse = { success: true };
    httpSpy.post.and.returnValue(of(mockResponse));
    service.createVenta(compra).subscribe(res => {
      expect(res.success).toBeTrue();
      done();
    });
  });
});
