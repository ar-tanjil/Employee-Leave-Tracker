import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { provideHotToastConfig } from '@ngxpert/hot-toast';
import { httpInterceptor } from './core/interceptors/http.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor, httpInterceptor,],)),
    provideHotToastConfig(
      {
        position: 'top-right',
        stacking: 'vertical',
        visibleToasts: 5,
        duration: 4000,
        dismissible: true,
        style: {
          padding: '14px',
          margin: '10px',
          marginTop: '60px',
          borderRadius: '8px',
        },
      }
    ),
  ],
};
