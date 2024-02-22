import { Component, OnInit } from '@angular/core';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  products: Product[] = [];

  constructor(private productService: ProductService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
        .subscribe(products => this.products = products);
  }
  add(name: string, price: string, quantity: string): void {
    name = name.trim();
    if (!name || !price || !quantity) { return; } // Ensure all fields are provided
    const priceNumber = parseFloat(price); // Convert price to float
    const quantityNumber = parseInt(quantity, 10); // Convert quantity to integer
    if (isNaN(priceNumber) || isNaN(quantityNumber)) {
      console.error('Price or quantity is not a valid number');
      return;
    }
    this.productService.addProduct({ name, price: priceNumber, quantity: quantityNumber } as Product)
    .subscribe(product => {
      this.products.push(product);
      this.messageService.add(`Added product: ${name}`); // Optional: log a message
    });
  }

  delete(product: Product): void {
    this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.id).subscribe();
  }
}
