import { Injectable } from '@angular/core';

import {LoginComponent} from './login/login.component';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import {User} from './user';


@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private loginUrl = 'http://localhost:8080/users/login'; // URL to login api
  private httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  isLoggedIn: boolean = false;
  isAdmin: boolean = false;
  isCust: boolean = false;

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {}

    login(username: string, password: string): Observable<string>{
      const params = new HttpParams()
        .set('username', username)
        .set('password', password);
    return this.http.post(this.loginUrl, {}, { params, responseType: 'text' })
      .pipe(
        map((response: string) => {
        if (response == 'admin login successful') {
          this.isLoggedIn = true;
          this.isAdmin = true;
        }
        else if (response == 'user login successful') {
          this.isLoggedIn = true;
          this.isCust = true;
        }

        return response;
      }),
        catchError(this.handleError<string>('login'))
      );
    }

    logout(): void {
       this.isLoggedIn = false;
       this.isAdmin = false;
       this.isCust = false;
    }

      /** Log a ProductService message with the MessageService */
    private log(message: string) {
      this.messageService.add(`LoginService: ${message}`);
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
