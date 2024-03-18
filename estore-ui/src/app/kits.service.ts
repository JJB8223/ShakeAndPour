import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Kit } from './kit';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class KitService {
  private kitsUrl = 'http://localhost:8080/kits';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {}

  /**GET products from the server */
  getKits(): Observable<Kit[]> {
    return this.http.get<Kit[]>(this.kitsUrl)
      .pipe(
        tap(_ => this.log('fetched Kits')),
        catchError(this.handleError<Kit[]>('getKits', []))
      );
  }

  /**DELETE: delete the product from the server */
  deleteKit(id: number): Observable<Kit> {
    const url = `${this.kitsUrl}/${id}`;

    return this.http.delete<Kit>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted Kit id=${id}`)),
      catchError(this.handleError<Kit>('deleteKit'))
    );
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
