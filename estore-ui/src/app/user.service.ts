import { Injectable } from '@angular/core';
import { User } from './user';
import { MessageService } from './message.service';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  username: string = ''
  user: User | undefined

  getUser(): Observable<User> {
    const url = `http://localhost:8080/users/getByUsername/${this.username}`;
    console.log(url)
    return this.http.get<User>(url)
    .pipe (
      tap((response) => console.log('User details:', response))
    )
  }

  setUsername(username: string): void {
    this.username = username;
  }

  getUsername(): string {
    return this.username;
  }

  getName(): string {
    if (this.user) {
      return this.user.name
    } else {
      return '';
    }
  }

   /** A logging helper method */
   private log(message: string) {
    this.messageService.add(`UserService: ${message}`);
    console.log(message)
  }
  constructor(private http: HttpClient, private messageService : MessageService ) {}
}
