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
    this.userService.changeUsername(this.id, username)
      .subscribe((username: string) => this.username = username);
  }

}
