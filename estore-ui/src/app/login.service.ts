import { Injectable } from '@angular/core';

import {LoginComponent} from './login/login.component';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import {User} from './user';
import { LoginResponse } from './login-response';


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
    private messageService: MessageService) {
      this.checkLoginStatus();
    }

    checkLoginStatus(): void {
      this.isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
      this.isAdmin = localStorage.getItem('role') === 'admin';
      this.isCust = localStorage.getItem('role') === 'user';
    }

    login(username: string, password: string): Observable<LoginResponse> {
      const params = new HttpParams()
        .set('username', username)
        .set('password', password);
  
      return this.http.post<LoginResponse>(this.loginUrl, {}, { params })
        .pipe(
          map(response => {
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('role', response.userType);
            localStorage.setItem('userId', response.userId.toString());
            this.checkLoginStatus();
  
            return response;
          }),
          catchError(this.handleError<LoginResponse>('login'))
        );
    }

    logout(): void {
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('role');
      this.checkLoginStatus();
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
