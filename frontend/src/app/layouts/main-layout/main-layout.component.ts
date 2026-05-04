import { Component, computed, HostListener, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLinkActive, RouterOutlet } from '@angular/router';
import { environment } from '../../../environments/environment';
import { NavbarComponent } from "../../core/navbar/navbar.component";
import { filter } from 'rxjs';

@Component({
  selector: 'ims-main-layout.component',
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css',
})
export class MainLayoutComponent {

  private currentUrl = signal<string>('');

  constructor(private router: Router) {

    // Listen route changes
    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.currentUrl.set(event.urlAfterRedirects);
      });
  }

  // Convert URL → Title Case
  currentPageTitle = computed(() => {
    const url = this.currentUrl();
    if (!url || url === '/') return 'Home';

    return url
      .split('?')[0]               // remove query params
      .split('/')                  // split path
      .filter(Boolean)             // remove empty parts
      .map(this.toTitleCase)       // convert each segment
      .join(' ');
  });

  private toTitleCase(text: string): string {
    return text
      .replace(/-/g, ' ')          // kebab-case → spaces
      .replace(/\b\w/g, c => c.toUpperCase());
  }
}
