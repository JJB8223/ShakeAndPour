import { HttpClient } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

interface LoginResponse {
  status: string;
  message: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  @Input() username: string = '';
  @Input() password: string = '';

  constructor(private http: HttpClient, private router: Router) {} //Router so I can send them to different pages, Httpclient so a http can ask login controller questions

  login(): void {
    this.http.post<LoginResponse>('/login/authenticate', { username: this.username, password: this.password })
      .subscribe(  //handles http stuff
        (response: LoginResponse) => {
          if (response.status === 'OK') {
            console.log('Login successful:', response.message); //console stuff will literally output it into the console
            this.router.navigate(['/dashboard']);
          } else {
            console.error('Login failed:', response.message);
          }
        },
        (error: any) => {
          console.error('Login failed:', error);
        }
      );
  }
}
