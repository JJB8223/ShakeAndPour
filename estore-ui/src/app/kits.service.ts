import { Injectable } from '@angular/core';
import { Kit } from './kit';
import { Observable, catchError, tap, of} from 'rxjs';
import { MessageService } from './message.service';

import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class KitsService {

  private KitsUrl = 'http://localhost:8080/kits';

  constructor(private http: HttpClient,
              private messageService: MessageService) { }


  getKits(): Observable<Kit[]> {
    return this.http.get<Kit[]>(this.KitsUrl)
      .pipe(
        tap(_=> this.log('Fetched Kits')),
        catchError(this.handleError<Kit[]>('getKits', []))
      );
  }

  /* GET Kits whose name contains search term */
  searchKits(term: string): Observable<Kit[]> {
    if (!term.trim()) {
      // if not search term, return empty Kit array.
      return of([]);
    }
    return this.http.get<Kit[]>(`${this.KitsUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
        this.log(`found Kits matching "${term}"`) :
        this.log(`no Kits matching "${term}"`)),
      catchError(this.handleError<Kit[]>('searchKits', []))
    );
  }

  // helper functions for the kits service class

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



}
