import { Component, HostListener, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { environment } from '../../../../environments/environment'
  import Swal from 'sweetalert2';import { AuthService } from '../../../core/services/auth.service';
;

@Component({
  selector: 'app-navbar',
  imports: [RouterLinkActive, RouterLink],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {

  private readonly authService = inject(AuthService);

  readonly applicationName = environment.appName || 'Employee Leave Tracker';

  isMenuOpen = signal(false);

  toggleMenu() {
    this.isMenuOpen.set(!this.isMenuOpen());
    // lock body scroll
    document.body.style.overflow = this.isMenuOpen() ? 'hidden' : 'auto';
  }

  closeMenu() {
    this.isMenuOpen.set(false);
    document.body.style.overflow = 'auto';
  }

  // ESC key closes menu
  @HostListener('document:keydown.escape')
  onEsc() {
    this.closeMenu();
  }


  // logout function
async confirmLogout() {
  const result = await Swal.fire({
    title: 'Logout',
    text: "Are you sure you want to logout?",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Logout',
    cancelButtonText: 'No'
  });

  if (result.isConfirmed) {
    // Perform action
    this.authService.logout();

    // Swal.fire('Deleted!', 'Your file has been removed.', 'success');
  }
}


}
