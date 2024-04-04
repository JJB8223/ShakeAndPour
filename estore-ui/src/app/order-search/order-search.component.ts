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

  orders$!: Observable<Order[]>;
  private searchKeywords = new Subject<string>();

  ngOnInit() {
    this.orders$ = this.searchKeywords.pipe(
      // wait 300ms after the user types to prevent unnecessary requests and lag
      debounceTime(300),

      // ignore if the searchbar isn't changed
      distinctUntilChanged(),
      // switch to new search observable each time the term changes
      switchMap((term: string) => this.orderService.searchOrders(term, this.user)),
    );
    /* .pipe(
      function to display kit name

        switchMap(kits => {
          const productObservables = kits.map(kit => {
            const observables = kit.products_in_kit.map(productId =>
              this.getSpecificProduct(productId)
            );
            return forkJoin(observables).pipe(
              map(productNames => {
                kit.products_in_kit = productNames;
                return kit;      // Do something with each kit and its corresponding quantity
              })
            );
          });
          return forkJoin(productObservables);
        })
      )*/
  }

  // add a search term to the searchKeywords stream
  search(term: string): void {
    this.searchKeywords.next(term);
    this.messageService.add(`attempting to search with term ${term}`);
    this.messageService.add(`Orders:${this.orders}`);
    this.messageService.add(`Search results:${this.orderService.searchOrders('Kit', this.user)}`);
  }

}
