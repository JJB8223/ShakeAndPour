import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {LoginService} from '../login.service'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form = this.formBuilder.group({
    username: [""],
    password: [""]
  });

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    ) {} //Router so I can send them to different pages, Httpclient so a http can ask login controller questions

  login(): void {

    const username: string = this.form.value.username || '';
    const password: string = this.form.value.password || '';

    this.loginService.login(
      username, password
    ).subscribe(
      user => {
        if (user && user.username === 'admin') {
          this.router.navigateByUrl('/admin');
        }
        else if (user) {
          this.router.navigateByUrl('/user');
        }
        else {
          alert('Invalid username or password');
        }
      }
    );
  }
}
