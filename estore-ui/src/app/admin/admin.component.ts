import { Component, OnInit } from '@angular/core';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { Kit } from '../kit';
import { KitService } from '../kits.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent{

  // these variables are updated to define which inventory management forms are visible
  isAddingProduct: boolean = false;
  isEditingProduct: boolean = false;
  isDeletingProduct: boolean = false;
  isAddingKit: boolean = false;
  isEditingKit: boolean = false;
  isDeletingKit: boolean = false;

  products: Product[] = [];
  kits: Kit[] = [];

  constructor(private productService: ProductService, private kitService: KitService, private messageService: MessageService) { }

  ngOnInit(): void {
    // initializing the list of products and list of kits
    this.getProducts(); 
    this.getKits();
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products);
  }

  getKits(): void{
    this.kitService.getKits()
      .subscribe(kits => this.kits = kits);
  }

  deleteProduct(id: string): void {
    this.productService.deleteProduct(parseInt(id)) // the entry field enforces only integers but still passes strings
      .subscribe(
        response => {
          this.getProducts(); // updating the products once we've received a response
        }
      );
  }

  addProduct(name: string, productPrice: string, productQuantity: string): void {
    // Couldn't find a clean way to only allow floats in the price field so this can cause an error
    this.productService.addProduct({ name, price: parseFloat(productPrice), quantity: parseInt(productQuantity) } as Product)
      .subscribe(
        response => {
          this.getProducts(); // updating the products once we've recevied a response
        }
      );
  }

  updateProduct(productID: string, name: string, productPrice: string, productQuantity: string): void {
    // Couldn't find a clean way to only allow floats in the price field so this can cause an error
    this.productService.updateProduct({id: parseInt(productID), name, price: parseFloat(productPrice), quantity: parseInt(productQuantity)} as Product)
      .subscribe(
        response => {
          this.getProducts(); // updating the products once we've received a response
        }
      );
  }

  deleteKit(id: string): void {
    this.kitService.deleteKit(parseInt(id)) // the entry field enforces only integers but still passes strings
      .subscribe(
        response => {
          this.getKits(); // updating the kits once we've received a response
        }
      );
  }

  addKit(name: string, kitPrice: string, kitQuantity: string, kitProductIDs: string): void {
    let idArray: number[] = [];
    for (let id of kitProductIDs.split(",")) {
      idArray.push(parseInt(id.trim())); // removing all whitespace and adding the parsed integer to the array of product ids
    }
    this.kitService.addKit({name, price: parseFloat(kitPrice), quantity: parseInt(kitQuantity), products_in_kit: idArray} as Kit)
      .subscribe(
        response => {
          this.getKits();
        }
      );
  }

}
