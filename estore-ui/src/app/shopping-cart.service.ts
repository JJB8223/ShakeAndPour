import { Injectable, numberAttribute } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MessageService } from './message.service';
import { Kit } from './kit';
import { KitMap } from './kit-map';
import { Observable, catchError, tap, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {

  constructor(private http: HttpClient,
              private messageService: MessageService) { }

  // setting the header opitions and url
  private ShoppingCartURL = 'http://localhost:8080/cart';
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  }
  
  addToShoppingCart(id: number): void {
    const url = `http://localhost:8080/cart/add/${id}/1`;
    console.log(url); // Check the constructed URL

    this.http.post(url, null).subscribe(
      (response) => {
        console.log('Item added to the shopping cart successfully:', response);
      },
    );
  }

  getShoppingCart(): Observable<KitMap[]> {
    return this.http.get<KitMap[]>(this.ShoppingCartURL)
    .pipe(
      tap(_=> this.log('fetched shopping cart')),
      catchError(this.handleError<KitMap[]>('getShoppingCart'))
    )
  }

  removeItem(id: number): void {
    const url = `http://localhost:8080/cart/remove/${id}/1`
    this.http.delete(url).subscribe(
      (response) => {
        console.log('Item removed from the shopping cart', response);
      }
    );
  }

  getTotalCost(): Observable<any> {
    const url = `${this.ShoppingCartURL}/total`;
    return this.http.get<number>(url, this.httpOptions)
      .pipe(
        tap(_ => this.log('fetched total cost of the shopping cart')),
        catchError(this.handleError<number>('getTotalCost', 0))
      );
  }

  /** A logging helper method */
  private log(message: string) {
    this.messageService.add(`ProductService: ${message}`);
    console.log(message)
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
