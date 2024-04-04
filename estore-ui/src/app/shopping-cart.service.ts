import { Injectable, numberAttribute } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { MessageService } from './message.service';
import { Kit } from './kit';
import { Order } from './order';
import { KitMap } from './kit-map';
import{UserService} from './user.service';
import { Observable, catchError, tap, of, switchMap, mergeMap, map, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  constructor(private http: HttpClient,
              private messageService: MessageService,
              private userService: UserService) { }

  // setting the header opitions and url
  private ShoppingCartURL = 'http://localhost:8080/cart';
  private orderURL = 'http://localhost:8080/orders';
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }

  private getUserId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? parseInt(userId, 10) : 0;
  }

  addToShoppingCart(kitId: number, quantity: number): Observable<any> {
    const userId = this.getUserId();
    const url = `${this.ShoppingCartURL}/add/${userId}/${kitId}/${quantity}`;
    return this.http.post<any>(url, null, this.httpOptions).pipe(
      tap((response) => console.log('Item added to the shopping cart successfully:', response))
    );
  }

  removeItem(kitId: number, quantity: number): Observable<any> {
    const userId = this.getUserId();
    const url = `${this.ShoppingCartURL}/remove/${userId}/${kitId}/${quantity}`;
    return this.http.delete<any>(url, this.httpOptions).pipe(
      tap((response) => console.log('Item removed from the shopping cart', response))
    );
  }

  getShoppingCart(): Observable<KitMap[]> {
    const userId = this.getUserId();
    const url = `${this.ShoppingCartURL}/${userId}`;
    return this.http.get<KitMap[]>(url, this.httpOptions).pipe(
      tap(_ => console.log('fetched shopping cart')),
      catchError(this.handleError<KitMap[]>('getShoppingCart'))
    );
  }

  getFullShoppingCart(): Observable<Kit[]> {
      const userId = this.getUserId();
      const url = `${this.ShoppingCartURL}/fullkits/${userId}`;
      return this.http.get<Kit[]>(url, this.httpOptions).pipe(
        tap(_ => console.log('fetched shopping cart')),
        catchError(this.handleError<Kit[]>('getShoppingCart'))
      );
  }

  getTotalCost(): Observable<any> {
    const userId = this.getUserId();
    const url = `${this.ShoppingCartURL}/total/${userId}`;
    return this.http.get<number>(url, this.httpOptions).pipe(
      tap(_ => this.log('fetched total cost of the shopping cart')),
      catchError(this.handleError<number>('getTotalCost', 0))
    );
  }

  createOrder(kitmap: Kit[]): Observable<Order> {
    const username = this.userService.getUsername();
    this.log(JSON.stringify(kitmap))
    const params = new HttpParams()
        .set('username', username)
        .set('kitsJson', JSON.stringify(kitmap));

    const order_url = `${this.orderURL}/create`;
     return this.http.post<Order>(order_url, params).pipe(
      tap(order => console.log("Order Created Successfully:", order)),
      catchError(this.handleError<Order>('createOrder'))
     );
  }

  clearCart(): Observable<any> {
    const userId = this.getUserId();
        const delete_url = `${this.ShoppingCartURL}/clear/${userId}`;
    return this.http.delete<any>(delete_url, this.httpOptions).pipe(
       tap(_ => this.log('Cart Has Been Cleared')),
           catchError(this.handleError<void>('getTotalCost'))
     );
  }

  purchaseCart(): Observable<any> {
     return this.getFullShoppingCart().pipe(

         switchMap(kit => {
            if (kit.length === 0) {
              alert("Nothing to Purchase!");
              return of([]);
            }
            return this.createOrder(kit).pipe(
                     switchMap(order => this.clearCart())
                   );


         }),
         catchError(this.handleError<any>('purchaseCart'))

       );
  }

  /** A logging helper method */
  private log(message: string) {
    this.messageService.add(`ProductService: ${message}`);
    console.log(message)
  }

  private handleErrorAlert<T>(message: string){
    alert(message)
    return "Not enough" as T;
  }

  /** Error Handling helper method */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

}
