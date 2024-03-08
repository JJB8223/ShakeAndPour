import { Injectable } from '@angular/core';

import {Login} from './login';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import {User} from './user';


@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private loginUrl = 'http://localhost:8080/login'; // URL to login api
  private httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {}

    login(username: string, password: string): Observable<User | null>{
      const url = `${this.loginUrl}/authenticate`
      const body = {username, password}
      return this.http.post<User>(url, body, this.httpOptions)
      .pipe(catchError(this.handleError<User>('login'))
      );
    }

      /** Log a ProductService message with the MessageService */
    private log(message: string) {
      this.messageService.add(`ProductService: ${message}`);
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
