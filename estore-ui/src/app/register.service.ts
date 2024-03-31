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
          catchError(this.handleError<User>('register'))
        );
    }

      /** Log a RegisterService message with the MessageService */
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
