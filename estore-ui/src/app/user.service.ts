import { Injectable } from '@angular/core';
import { User } from './user';
import { MessageService } from './message.service';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, of, switchMap, tap, throwError } from 'rxjs';

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
    const url = `http://localhost:8080/users/update/${id}/u`;
    const params = new HttpParams().set('username', newUsername);
    return this.http.put<any>(url, {}, { params }).pipe(
      tap(() => {
        this.setUsername(newUsername);
      }),
      catchError(this.handleError('changeUsername'))
    )
  }

  changePassword(id : number, newPassword : string): Observable<any> {
    const url = `http://localhost:8080/users/update/${id}/${newPassword}`;
    return this.http.put<any>(url, {}).pipe(
      tap(() => {
        this.setUsername(newPassword);
      }),
      catchError(this.handleError('changePassword'))
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

  private handleError(operation = 'operation') {
    return (error: HttpErrorResponse): Observable<never> => {
      if (error.status === 400) {
        console.error(`${operation} failed due to a bad request: `, error.error);
      } else {
        // Log generic error messages or other specific errors
        console.error(`${operation} failed: `, error.message);
      }
      return throwError(() => error);
    }
  }
  
  constructor(private http: HttpClient, private messageService : MessageService ) {}
}
