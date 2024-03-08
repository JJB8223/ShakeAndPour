import { HttpClient } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  form: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  constructor(private http: HttpClient, private router: Router) {} //Router so I can send them to different pages, Httpclient so a http can ask login controller questions

  login(): void {
    let user = this.authService.login(
      this.form.value.username,
      this.form.value.password
    );

    if(!user){
      alert('Invalid username or password');
    }
    else if (user.username === "admin"){
    this.router.navigateByUrl('/admin');
    }
    else{
    this.router.navigateByUrl('/user');
    }


  }
}
