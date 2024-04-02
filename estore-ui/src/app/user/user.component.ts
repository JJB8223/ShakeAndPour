import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {
  username: string = ''
  name: string = ''
  password: string = ''
  id: number = 0
  isChangingUser: boolean = false;
  isChangingPassword: boolean = false;
  isChangingName: boolean = false;
  successMessage: string = '';

  constructor(private userService: UserService) {}
  

  ngOnInit(): void {
    this.userService.userDetails$.subscribe({
      next: userDetails => {
        if (userDetails) {
          this.username = userDetails.username;
          this.name = userDetails.name;
          this.id = userDetails.id;
          this.password = userDetails.password;
        }
      }
    });
  }

  changeUsername(username: string): void {
    if (!username.trim()) {
      alert('Username cannot be blank');
      return;
    }
    this.userService.changeUsername(this.id, username).subscribe({
        next: (response) => {
          this.successMessage = 'Username successfully changed to ' + username;
          (username: string) => this.username = username;
        },
        error: (error) => {
          if (error.status === 400) {
            alert('This username is taken. Please try another.');
          } else {
            alert('Failed to change username. Please try again.');
          }
        }
    });
  }

  changePassword(password: string): void {
    if (!password.trim()) {
      alert('Password cannot be blank');
      return;
    }
    this.userService.changePassword(this.id, password).subscribe({
      next: (response) => {
        this.successMessage = 'Password successfully changed to ' + password;
        this.password = password;
      },
      error: (error) => {
        alert('Failed to change password. Please try again.')
      }
    })
  }

  changeName(name: string): void {
    if (!name.trim()) {
      alert('Name cannot be blank');
      return;
    }
    this.userService.changeName(this.id, name).subscribe({
      next: (response) => {
        this.successMessage = 'Name successfully changed to ' + name;
        this.name = name;
      },
      error: (error) => {
        alert('Failed to change name. Please try again.')
      }
    })
  }

}
