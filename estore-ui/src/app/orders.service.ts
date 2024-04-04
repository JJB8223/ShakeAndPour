import { Injectable } from '@angular/core';
import { Observable, catchError, of, tap } from 'rxjs';
import { Kit } from './kit';
import { Order } from './order';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {
  ordersUrl : string = "http://localhost:8080/orders/"
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  }
  
  getOrders(user : string): Observable<Order[]> {
    return this.http.get<Order[]>(this.ordersUrl + user)
      .pipe(
        tap(_ => this.log('fetched Orders')),
        catchError(this.handleError<Order[]>('getOrders', []))
      );
  }
  constructor(private http : HttpClient, private messageService : MessageService) { }

  addOrders(newOrder: Order): Observable<Order> {
    return this.http.post<Order>(`${this.ordersUrl}/create`, newOrder, this.httpOptions).pipe(
      tap((newOrdered: Order) => this.log(`added new order to history with ID ${newOrdered.id}`)),
      catchError(this.handleError<Order>('addOrder'))
    )
  }



  private log(message: string) {
    this.messageService.add(`KitService: ${message}`);
  }

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
