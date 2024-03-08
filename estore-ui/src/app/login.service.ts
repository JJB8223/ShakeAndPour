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

    login(username: string, password: string){
    let user = (u) =>u.username===username && u.password===password;
      if(this.username)
    }
}
