import { Injectable } from '@angular/core';
import { User } from './user';
import { MessageService } from './message.service';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, of, switchMap, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  username: string = ''
  user: User | undefined
  private usernameSource = new BehaviorSubject<string>(localStorage.getItem('username') || '');
  username$ = this.usernameSource.asObservable();

  setUsername(username: string): void {
    localStorage.setItem('username', username);
    this.usernameSource.next(username);
  }

  getUser(username: string): Observable<User> {
    const url = `http://localhost:8080/users/getByUsername/${username}`;
    return this.http.get<User>(url);
  }
  
  getUsername(): string {
    if (!this.username) {
      this.username = localStorage.getItem('username') || '';
    }
    return this.username;
  }

  getName(): string {
    if (this.user) {
      return this.user.name
    } else {
      return '';
    }
  }

  changeUsername(id : number, newUsername : string): Observable<any> {
    const url = `http://localhost:8080/users/update/${id}/${newUsername}`;
    return this.http.put<any>(url, {}).pipe(
      tap(() => {
        this.setUsername(newUsername);
      }),
      catchError(this.handleError('changeUsername'))
    )
  }

  userDetails$: Observable<User | null> = this.username$.pipe(
    switchMap(username => username ? this.getUser(username) : of(null)),
    catchError(() => of(null)) // Handle potential errors, e.g., user not found
  );

   /** A logging helper method */
   private log(message: string) {
    this.messageService.add(`UserService: ${message}`);
    console.log(message)
  }

  /** Error Handling helper method */
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
  
  constructor(private http: HttpClient, private messageService : MessageService ) {}
}
