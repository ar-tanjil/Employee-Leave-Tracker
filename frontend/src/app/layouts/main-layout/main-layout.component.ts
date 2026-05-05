import { Component, computed, HostListener, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { NavbarComponent } from '../../core/components/navbar/navbar.component';



interface Breadcrumb {
  label: string;
  url: string;
}

@Component({
  selector: 'app-main-layout',
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


  // 👉 Breadcrumb generator
  breadcrumbs = computed<Breadcrumb[]>(() => {
    const url = this.currentUrl();

    if (!url || url === '/') {
      return [{ label: 'Home', url: '/' }];
    }

    const segments = url.split('?')[0].split('/').filter(Boolean);

    const result: Breadcrumb[] = [
      { label: 'Home', url: '/' }
    ];

    let path = '';

    for (const segment of segments) {
      path += `/${segment}`;

      result.push({
        label: this.toTitle(segment),
        url: path
      });
    }

    return result;
  });

  // Convert "employee-dashboard" → "Employee Dashboard"
  private toTitle(text: string): string {
    return text
      .replace(/-/g, ' ')
      .replace(/\b\w/g, c => c.toUpperCase());
  }

  // Navigation handler
  navigateTo(url: string): void {
    this.router.navigateByUrl(url);
  }
}
