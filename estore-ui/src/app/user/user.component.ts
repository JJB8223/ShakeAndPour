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
  isChangingUser: boolean = false;
  isChangingPassword: boolean = false;

  constructor(private userService: UserService) {}
  

  ngOnInit(): void {
    this.username = this.userService.getUsername()
    this.userService.getUser().subscribe(user => {
      this.username = user.username;
      this.name = user.name;
    });
  }

}
