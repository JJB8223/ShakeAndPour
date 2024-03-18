import { Component, OnInit } from '@angular/core';

import { ProductSearchComponent } from '../product-search/product-search.component';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent{

  isAddingProduct: boolean = false;
  isEditingProduct: boolean = false;
  isDeletingProduct: boolean = false;

  products: Product[] = [];

  constructor(private productService: ProductService, private messageService: MessageService) { }

  ngOnInit(): void {
    this.getProducts(); // initializing the list of products 
  }

  getProducts(): void {
    this.productService.getProducts()
        .subscribe(products => this.products = products);
  }

  deleteProduct(id: string): void {
    this.productService.deleteProduct(parseInt(id)) // the entry field enforces only integers but still passes strings
      .subscribe(
        response => {
          this.getProducts(); // updating the products once we've received a response for the deletion operation
        }
      )
  }

}
