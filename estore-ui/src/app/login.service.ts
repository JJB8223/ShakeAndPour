import { Injectable } from '@angular/core';

import {Login} from './login';

import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private loginUrl = 'http://localhose:8080/login'; // URL to login api
  httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {}

    /** POST: Check whose logging in, either admin or customer
}
