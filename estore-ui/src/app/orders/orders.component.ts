import { Component } from '@angular/core';
import { OrdersService } from '../orders.service';
import { UserService } from '../user.service';
import { Order } from '../order';
import { User } from '../user';
import { Observable, forkJoin, map, switchMap } from 'rxjs';
import { KitService } from '../kits.service';
import { ProductService } from '../product.service';
import { ShoppingCartService } from '../shopping-cart.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.css'
})
export class OrdersComponent {
  orders : Order[] = [];
  user : string = ''
  constructor(private orderService : OrdersService, private userService : UserService, private kitService : KitService, private productService : ProductService, private shoppingCartService: ShoppingCartService) {
    const username = localStorage.getItem('username');
    if (username) {
      this.user = username; 
    }
  }

  ngOnInit() {
    this.getOrders();
  }

  getSpecificKit(id : number) {
    return this.kitService.getKit(id).pipe(
      map(kit => kit.name)
  );
  }

  getOrders() : void {
    this.orderService.getOrders(this.user)
      .subscribe(orders => {
        orders.forEach(order => {
          order.kits_in_order.forEach(kit => {
            const productObservables = kit.products_in_kit.map((productId: number) =>
              this.productService.getProduct(productId));
            forkJoin(productObservables).subscribe(products => {
              kit.products_in_kit = products;
            });
          });
        });
        this.orders = orders;
      });
  }

  addToShoppingCart(id: number, quantity: number): void {
    console.log("This is the number we are adding: " + id);
    this.shoppingCartService.addToShoppingCart(id, quantity).subscribe({
      next: (response) => {
        console.log('Response from adding to cart:', response);
      },
      error: (err) => {
        console.error('Error adding item to cart:', err);
      }
    });
  }

}
