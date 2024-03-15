import { Component } from '@angular/core';
import { KitMap } from '../kit-map';
import { ShoppingCartService } from '../shopping-cart.service';
import { KitsService } from '../kits.service';
import { switchMap } from 'rxjs';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {
  constructor (private shoppingCartService: ShoppingCartService,
               private kitService: KitsService ) { }
  kitMaps: KitMap[] = []

  ngOnInit() {
    this.getShoppingCart()
  }



  getShoppingCart(): void {
    this.shoppingCartService.getShoppingCart()
      .subscribe(kitMap => this.kitMaps = kitMap)
  }

  removeItem(id: number): void {
    console.log(id)
    this.shoppingCartService.removeItem(id)
    location.reload()
  }

}