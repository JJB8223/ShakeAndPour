import { Injectable } from '@angular/core';

import {RegisterComponent} from './register/register.component';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import {User} from './user';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private registerUrl = 'http://localhost:8080/users/register'; // URL to login api
   private httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

    /** POST: add a new user to the server */
    register(user: User): Observable<string>{
        return this.http.post<User>(`${this.registerUrl}`, user, this.httpOptions).pipe(
          tap((newUser: User) => this.log(`New User Registered with id=${newUser.id}`)),
          catchError(this.handleError)
        );
    }

      /** Log a RegisterService message with the MessageService */
    private log(message: string) {
      this.messageService.add(`LoginService: ${message}`);
    }

    private handleError(error: HttpErrorResponse) {
      if (error.status === 400) {
        return throwError('Username is already in use!');
      } else if (error.status === 409) {
        return throwError('Conflict occurred when creating new user.');
      } else if (error.status === 500) {
        return throwError('Internal Server Error. Please try again later.');
      } else {
        return throwError('Something went wrong. Please try again later.');
      }
    }

}
