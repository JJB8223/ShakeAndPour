import { Component } from '@angular/core';
import { OrdersService } from '../orders.service';
import { KitService } from '../kits.service';
import { ProductService } from '../product.service';
import { Order } from '../order';
import { Observable, Subject, debounceTime, distinctUntilChanged, forkJoin, map, switchMap } from 'rxjs';
import { Kit } from '../kit';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-order-search',
  templateUrl: './order-search.component.html',
  styleUrl: './order-search.component.css'
})
export class OrderSearchComponent {

  constructor(private orderService: OrdersService, private kitService : KitService, 
    private productService : ProductService, private messageService : MessageService) {
      const username = localStorage.getItem('username');
      if (username) {
        this.user = username; 
      }
  }

  orders: Order[] = [];
  user : string = '';

  // get all orders the currently logged in user has made, then filter based on the entered search term
  search(term: string): void {
    debounceTime(300); // delaying for 300 milliseconds to avoid unnecessary API calls

    this.orderService.getOrders(this.user)
      .subscribe(orders => {

        let finalOrders : Order[] = [] // this array contains the orders that will ultimately be displayed based on the filter results
        orders.forEach(order => {
          let addOrder : boolean = false; // this indicates whether we ultimately add this order to the search results

          order.kits_in_order.forEach(kit => {

            let lowercaseName: string = kit.name.toLowerCase();
            if (lowercaseName.includes(term.toLowerCase())) {  // checking if this kit contains the entered search terms
              addOrder = true; // if the kit contains the entered search terms, the order is added to the search results
            }

            const productObservables = kit.products_in_kit.map((productId: number) =>
            this.productService.getProduct(productId));
            forkJoin(productObservables).subscribe(products => {
              kit.products_in_kit = products;
            });
          });
          if (addOrder) { // checking if this order contained kits with the entered search terms
            finalOrders.push(order);
          }
        });
        this.orders = finalOrders;
      });
  }

}
