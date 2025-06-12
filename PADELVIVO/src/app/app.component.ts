import {
  Component,
  OnInit,
  OnDestroy,
  HostListener
} from '@angular/core';
import {
  Router,
  NavigationEnd,
  RouterOutlet,
  RouterLink,
  RouterLinkActive
} from '@angular/router';
import { AuthService } from './services/auth.service';
import { CommonModule, NgIf } from '@angular/common';
import { Subscription, filter } from 'rxjs';
import { NotificationPanelComponent } from './components/user/notificationpanel/notificationpanel.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgIf, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  isLoggedIn = false;
  isAdminLoggedIn = false;
  isUserLoggedIn = false;
  isMenuOpen = false;

  currentTitle: string = '';
  isMobile: boolean = false;

  showNotifications = false;


  private subs: Subscription[] = [];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.checkScreenSize();

    // Suscripciones
    this.subs.push(
      this.authService.isAuthenticated.subscribe(auth => {
        this.isLoggedIn = auth;
      }),
      this.authService.role.subscribe(role => {
        this.isAdminLoggedIn = role === 'ROLE_ADMIN';
        this.isUserLoggedIn = role === 'ROLE_USER';
      }),
      this.router.events
        .pipe(filter(event => event instanceof NavigationEnd))
        .subscribe((event: NavigationEnd) => {
          this.updateTitleFromRoute(event.urlAfterRedirects);
        })
    );
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth < 768;
  }

  updateTitleFromRoute(url: string) {
    if (url.includes('/torneos')) {
      this.currentTitle = 'Torneos';
    } else if (url.includes('/temporadas')) {
      this.currentTitle = 'Temporadas';
    } else if (url.includes('/partidos')) {
      this.currentTitle = 'Partidos';
    } else if (url.includes('/user/favoritos')) {
      this.currentTitle = 'Favoritos';
    } else if (url.includes('/user/perfil')) {
      this.currentTitle = 'Perfil';
    } else if (url.includes('/admin/actualizaciones')) {
      this.currentTitle = 'Actualizaciones';
    } else if (url.includes('/admin/torneos')) {
    this.currentTitle = 'Torneos';
    } else if (url.includes('/admin/temporadas')) {
    this.currentTitle = 'Temporadas';
    } else if (url.includes('/admin/partidos')) {
    this.currentTitle = 'Partidos';
    } else if (url.includes('/admin/jugadores')) {
      this.currentTitle = 'Jugadores';
      } else if (url.includes('/admin/users')) {
      this.currentTitle = 'Usuarios';
    } else {
      this.currentTitle = '';
    }
  }

  logout(): void {
    this.authService.logout();
     this.router.navigate(['/login']);
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  goBack() {
    window.history.back();
  }

  ngOnDestroy(): void {
    this.subs.forEach(sub => sub.unsubscribe());
  }
  toggleNotifications() {
  this.showNotifications = !this.showNotifications;
}
}
