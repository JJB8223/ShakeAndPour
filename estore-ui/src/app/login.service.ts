import { Injectable } from '@angular/core';

import {Login} from './login';

import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import {User} from './user'


@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private loginUrl = 'http://localhost:8080/login'; // URL to login api
  public user: Observable<User | null>;
  httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {}

    /** POST: Check whose logging in, either admin or customer */
    authenticateUser(username: string, password: string) {
      const url = `${this.loginUrl}/authenticate`;
      return this.http.post<User>(url, username, password, this.httpOptions)
      .pipe(map(user => {
      this.log(`User authenticated: ${user.username}`);
      return user;
      }),
      catchError(this.handleError<User>('authenticateUser')
    }



    private handleError<T>(operation = 'operation', result?: T) {
      return (error:any): Observable<T> => {

        console.error(error);
        this.log(`${operation} failed: ${error.message}`);

        return of(result as T);

      }
    }
}
