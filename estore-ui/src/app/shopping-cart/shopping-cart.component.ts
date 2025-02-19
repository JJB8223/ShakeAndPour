import { Component } from '@angular/core';
import { KitMap } from '../kit-map';
import { ShoppingCartService } from '../shopping-cart.service';
import { switchMap } from 'rxjs';
import { OnInit } from '@angular/core';
import {Order} from '../order';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {
  constructor (private shoppingCartService: ShoppingCartService,) { }
  kitMaps: KitMap[] = []

  totalCost: number = 0;

  ngOnInit() {
    this.getShoppingCart()
    this.getTotalCost()
  }



  getShoppingCart(): void {
    this.shoppingCartService.getShoppingCart()
      .subscribe(kitMap => this.kitMaps = kitMap)
  }

  addItem(id: number, quantity: number): void {
    console.log(id);
    this.shoppingCartService.addKitToShoppingCart(id, quantity).pipe(
      switchMap(() => this.shoppingCartService.getShoppingCart())
    ).subscribe(kitMap => {
      this.kitMaps = kitMap;
      this.getTotalCost();
    });
  }

  removeItem(id: number, quantity: number): void {
    console.log(id);
    this.shoppingCartService.removeItem(id, quantity).pipe(
      switchMap(() => this.shoppingCartService.getShoppingCart())
    ).subscribe(kitMap => {
      this.kitMaps = kitMap;
      this.getTotalCost();
    });
  }

  getTotalCost(): void {
    this.shoppingCartService.getTotalCost().subscribe({
      next: (cost) => {
        this.totalCost = cost; // Update totalCost with the value received from the service
      },
      error: (error) => {
        console.error('Error fetching total cost', error);
      }
    })
  }

  purchaseCart(): void {
    this.shoppingCartService.purchaseCart().subscribe(
      response => {
      if (response && response.length === 0){
        console.log("Shopping Cart is empty!");
      }
      else{
        alert("Shopping Cart Purchased!");
        this.getShoppingCart();
        this.getTotalCost();
      }
      },
      error => {
        console.error("Error occurred while purchasing cart:", error);
        alert("There was an error in purchasing your cart");
      }


    );
  }

}
