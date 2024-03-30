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

  userDetails$: Observable<User | null> = this.username$.pipe(
    switchMap(username => username ? this.getUser(username) : of(null)),
    catchError(() => of(null)) // Handle potential errors, e.g., user not found
  );

   /** A logging helper method */
   private log(message: string) {
    this.messageService.add(`UserService: ${message}`);
    console.log(message)
  }
  constructor(private http: HttpClient, private messageService : MessageService ) {}
}
