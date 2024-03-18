import { Injectable } from '@angular/core';
import { Product } from './product';

import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private ProductsUrl = 'http://localhost:8080/inventory';  // URL to web api
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }
  /** GET products from the server */
  /** GET products from the server */
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.ProductsUrl)
      .pipe(
        tap(_ => this.log('fetched Products')),
        catchError(this.handleError<Product[]>('getProducts', []))
      );
  }
  /** GET Product by id. Will 404 if id not found */
  getProduct(id: number): Observable<Product> {
    const url = `${this.ProductsUrl}/${id}`;
    return this.http.get<Product>(url).pipe(
      tap(_ => this.log(`fetched Product id=${id}`)),
      catchError(this.handleError<Product>(`getProduct id=${id}`))
    );
  }

  /** POST: add a new Product to the server */
  addProduct(Product: Product): Observable<Product> {
    return this.http.post<Product>(this.ProductsUrl, Product, this.httpOptions).pipe(
      tap((newProduct: Product) => this.log(`added Product w/ id=${newProduct.id}`)),
      catchError(this.handleError<Product>('addProduct'))
    );
  }
  /** DELETE: delete the Product from the server */
  deleteProduct(id: number): Observable<Product> {
    const url = `${this.ProductsUrl}/${id}`;

    return this.http.delete<Product>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted Product id=${id}`)),
      catchError(this.handleError<Product>('deleteProduct'))
    );
  }
  /* GET Products whose name contains search term */
  searchProducts(term: string): Observable<Product[]> {
    if (!term.trim()) {
      // if not search term, return empty Product array.
      return of([]);
    }
    return this.http.get<Product[]>(`${this.ProductsUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
        this.log(`found Products matching "${term}"`) :
        this.log(`no Products matching "${term}"`)),
      catchError(this.handleError<Product[]>('searchProducts', []))
    );
  }
    /** Log a ProductService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`ProductService: ${message}`);
  }

  /**
  * Handle Http operation that failed.
  * Let the app continue.
  *
  * @param operation - name of the operation that failed
  * @param result - optional value to return as the observable result
  */
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
  /** PUT: update the Product on the server */
  updateProduct(Product: Product): Observable<any> {
    return this.http.put(this.ProductsUrl, Product, this.httpOptions).pipe(
      tap(_ => this.log(`updated Product id=${Product.id}`)),
      catchError(this.handleError<any>('updateProduct'))
    );
  }
}
