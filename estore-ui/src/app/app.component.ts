import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Shake And Pour';

  constructor(private router: Router) {}

    // Method to check if the current route is /admin or /user
    isUserOrAdminPage(): boolean {
      return this.router.url === '/admin' || this.router.url === '/user';
    }
}
