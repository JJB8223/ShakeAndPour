import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { Kit } from '../kit';
import { ProductService } from '../product.service';
import { KitService } from '../kits.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: [ './dashboard.component.css' ]
})
export class DashboardComponent implements OnInit {
  products: Product[] = [];
  kits: Kit[] = []; 

  constructor(
    private productService: ProductService,
    private kitService: KitService 
  ) { }

  ngOnInit(): void {
    this.getProducts();
    this.getKits(); 
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(1, 5));
  }

  getKits(): void {
    this.kitService.getKits() 
      .subscribe(kits => this.kits = kits.slice(1, 5));
  }
}
