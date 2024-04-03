import { Component } from '@angular/core';
import { Observable, map, Subject } from 'rxjs';
import { catchError, forkJoin, switchMap } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Router } from '@angular/router';
import {FormBuilder} from '@angular/forms';
import { User } from '../user';
import {RegisterService} from '../register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
    users$!: Observable<User[]>;
    form = this.formBuilder.group({
      username: [''],
      password: [''],
      name: [''],
    });

    constructor(
      private router: Router,
      private formBuilder: FormBuilder,
      private registerService: RegisterService
    ) {}


    register(): void {

      const username: string = this.form.value.username || '';
      const password: string = this.form.value.password || '';
      const name: string = this.form.value.name || '';

      if (username == '' || password == '' || name == ''){
        alert("No fields can be left blank!");
      }
      else{
         this.registerService.register(
         username, password, name).subscribe(
            response => {
              console.log("Registration successful for user", username);
              alert("Registration Successful!");
            },
            error => {

              console.error("Registration error:", error);
              alert(error)
            }


         )
      }

    }
}
