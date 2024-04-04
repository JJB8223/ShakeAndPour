import { Component, OnInit} from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { Kit } from '../kit';
import { Order } from '../order';
import { Observable } from 'rxjs';
import { forkJoin, map} from 'rxjs';
import { OrdersService } from '../orders.service';
import { UserService } from '../user.service';


@Component({
  selector: 'app-custom-kit',
  templateUrl: './custom-kit.component.html',
  styleUrl: './custom-kit.component.css'
})
export class CustomKitComponent {


  products: Product[] = [];
  product_recieved : Product = {} as Product;
  customizedKit: Kit = {} as Kit;

  constructor(private productService: ProductService, private orderService: OrdersService, private user: UserService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products)
  }

  addKit(addedName : string, addedProductIDs : string): void{
    let total_price = 0
    console.log(addedName)
    let idArray: number[] = this.parseIDString(addedProductIDs)
    console.log(idArray)
    // get the price of the total products
    this.totalPrice(idArray)
    .subscribe(total => {
      total_price = total;
      this.customizedKit.price = parseFloat(total_price.toFixed(2))
      this.customizedKit.name = addedName
      this.customizedKit.id = 10000
      this.customizedKit.quantity = 20
      this.customizedKit.products_in_kit = idArray
      this.orderCustomKit(this.customizedKit)
      console.log('Total price:', total_price.toFixed(2));
    });
  }

  private orderCustomKit(customKit: Kit): void {
    let add_to_order : Kit[] = []
    add_to_order.push(customKit)

    let newOrder : Order = {
      id: 10000,
      user: this.user.getUsername(),
      kits_in_order: add_to_order
    }
    console.log(newOrder)
    this.orderService.addOrders(newOrder)
      .subscribe(
        response => {
          console.log('Purchased kit')
        }
      )
  }


  private totalPrice(idArray: number[]): Observable<number> {
    const observables: Observable<any>[] = [];
    for (const id of idArray) {
      observables.push(this.productService.getProduct(id));
    }
    return forkJoin(observables).pipe(
      map(products => {
        let total = 0;
        for (const product of products) {
          total += product.price;
        }
        return total;
      })
    );
  }


  private parseIDString (IDstring: string): number[] {
    let idArray: number[] = [];
    for (let id of IDstring.split(",")) {
      idArray.push(parseInt(id.trim())); // removing all whitespace and adding the parsed integer to the array of product ids
    }
    return idArray;
  }
}
