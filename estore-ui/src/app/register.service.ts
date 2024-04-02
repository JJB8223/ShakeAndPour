import { Injectable } from '@angular/core';

import {RegisterComponent} from './register/register.component';
import { Observable, of, throwError } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClientModule, HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import {User} from './user';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private registerUrl = 'http://localhost:8080/users/register'; // URL to login api
   private httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

  constructor(
  private router: Router,
    private http: HttpClient,
    private messageService: MessageService) { }

    /** POST: add a new user to the server */
    register(username: string, password: string, name: string): Observable<User>{
        return this.http.post<User>(`${this.registerUrl}/?username=${username}&password=${password}&name=${name}`,
         this.httpOptions).pipe(
          tap((newUser: User) => {
          this.log(`New User Registered with username=${newUser.username}`);
          this.router.navigateByUrl('/login');

          }),
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
