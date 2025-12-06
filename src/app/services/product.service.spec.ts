import { TestBed } from '@angular/core/testing';
import { ProductService } from './product.service';
import { ApiService } from './api.service';
import { of } from 'rxjs';

describe('ProductService - Gestión de Productos', () => {
  let service: ProductService;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('ApiService', ['getProductos', 'createProducto', 'updateProducto', 'deleteProducto']);
    TestBed.configureTestingModule({
      providers: [
        ProductService,
        { provide: ApiService, useValue: spy }
      ]
    });
    service = TestBed.inject(ProductService);
    apiServiceSpy = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
  });

  it('debería crear un producto exitosamente', (done) => {
    const newProduct = { name: 'Manzana', price: 3, stock: 20 };
    // El mock debe tener 'nombre', 'precio', 'stock'
    const mockCreated = { id: 1, nombre: 'Manzana', precio: 3, stock: 20 };
    apiServiceSpy.createProducto.and.returnValue(of(mockCreated));
    service.createProduct(newProduct).subscribe(product => {
      expect(product.id).toBe(1);
      expect(product.name).toBe('Manzana');
      expect(product.price).toBe(3);
      expect(product.stock).toBe(20);
      done();
    });
  });

  it('debería actualizar un producto', (done) => {
    const updateData = { name: 'Manzana Roja', price: 4, stock: 15 };
    const mockUpdated = { id: 1, nombre: 'Manzana Roja', precio: 4, stock: 15 };
    apiServiceSpy.updateProducto.and.returnValue(of(mockUpdated));
    service.updateProduct(1, updateData).subscribe(product => {
      expect(product.name).toBe('Manzana Roja');
      expect(product.price).toBe(4);
      expect(product.stock).toBe(15);
      done();
    });
  });

  it('debería eliminar un producto', (done) => {
    apiServiceSpy.deleteProducto.and.returnValue(of({ success: true }));
    service.deleteProduct(1).subscribe(result => {
      expect(result.success).toBeTrue();
      done();
    });
  });
});
