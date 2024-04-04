import { Injectable } from '@angular/core';
import { Observable, catchError, of, tap } from 'rxjs';
import { Kit } from './kit';
import { Order } from './order';
import { HttpClient } from '@angular/common/http';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {
  ordersUrl : string = "http://localhost:8080/orders/"
  
  getOrders(user : string): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.ordersUrl}?user=${user}`)
      .pipe(
        tap(_ => this.log('fetched Orders')),
        catchError(this.handleError<Order[]>('getOrders', []))
      );
  }

  searchOrders(term: string, user : string): Observable<Order[]> {
    if (!term.trim()) {
      // if not search term, return empty Kit array.
      return of([]);
    }
    let url: string = `${this.ordersUrl}${term}/?user=${user}`;
    this.log(`Request url: ${url}`);
    let response : Observable<Order[]> = this.http.get<Order[]>(url).pipe(
      tap(x => x.length ?
        this.log(`found Orders matching "${term}"`) :
        this.log(`no Orders matching "${term}"`)),
      catchError(this.handleError<Order[]>('searchOrders', []))
    );
    this.log(`${response}`);
    return response;
  }
  constructor(private http : HttpClient, private messageService : MessageService) { }

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
