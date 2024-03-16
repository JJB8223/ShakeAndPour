import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import {LoginService} from './login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Shake And Pour';

  constructor(
  private router: Router,
  private loginService: LoginService
  ) {}

    // Method to check if the current route is /admin or /user
    isAdmin(): boolean {
      return this.loginService.isAdmin;
    }

    // Method to check if a user is logged admin
    isLoggedIn(): boolean {
      return this.loginService.isLoggedIn;
    }

    logout(): void {
      this.loginService.logout();
      // Redirect to dashboard after logout
      this.router.navigateByUrl('/dashboard');
    }
}
