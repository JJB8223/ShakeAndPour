import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {LoginService} from '../login.service'
import {UserService} from '../user.service'
import { Observable } from 'rxjs';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  users$!: Observable<User[]>;
  form = this.formBuilder.group({
    username: [''],
    password: ['']
  });

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private userService: UserService
    ) {}

    login(): void {
      const username: string = this.form.value.username || '';
      const password: string = this.form.value.password || '';
  
      this.loginService.login(username, password).subscribe({
          next: (response) => {
              // Assuming response.userType tells if the user is an admin or a regular user
              if (response.userType === 'admin') {
                  this.router.navigateByUrl('/admin');
              } else if (response.userType === 'user') {
                  this.userService.setUsername(username);
                  this.router.navigateByUrl('/user');
              } else {
                  // Handle unexpected userType
                  console.error('Unexpected user type received', response);
                  alert('Invalid login attempt');
              }
          },
          error: (err) => {
              // Handle HTTP errors or unsuccessful login attempts
              console.error('Login failed', err);
              alert('Invalid username or password');
          }
      });
  }
  
}
