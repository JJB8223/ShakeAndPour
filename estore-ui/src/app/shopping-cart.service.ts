import { Injectable } from '@angular/core';
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
  

  // method to add things to the shopping cart
  addToShoppingCart(id: Number): void {
    // leaving this shit alone for now
  }

  getShoppingCart(): Observable<KitMap[]> {
    return this.http.get<KitMap[]>(this.ShoppingCartURL)
    .pipe(
      tap(_=> this.log('fetched shopping cart')),
      catchError(this.handleError<KitMap[]>('getShoppingCart'))
    )
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
