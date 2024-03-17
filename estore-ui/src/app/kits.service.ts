import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Kit } from './kit';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class KitService {
  private kitsUrl = 'http://localhost:8080/kits';

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {}

  getKits(): Observable<Kit[]> {
    return this.http.get<Kit[]>(this.kitsUrl)
      .pipe(
        catchError(this.handleError<Kit[]>('getKits', []))
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
