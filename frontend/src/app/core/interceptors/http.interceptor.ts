import { HttpInterceptorFn, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HotToastService } from '@ngxpert/hot-toast';
import { NgxSpinnerService } from 'ngx-spinner';
import { catchError, throwError, finalize, tap, retry } from 'rxjs';
import { ApiError, ApiResponse } from '../../models/api-response.model';
import { LoadingService } from '../services/loading.service';

export const httpInterceptor: HttpInterceptorFn = (req, next) => {
  const toast = inject(HotToastService);
  const loadingService = inject(LoadingService);

  const isModifyRequest = ['POST', 'PUT', 'PATCH', 'DELETE'].includes(req.method);

  loadingService.show();

  return next(req).pipe(
    tap((event) => {
      if (event instanceof HttpResponse && isModifyRequest) {
        const body = event.body as ApiResponse<any>;
        toast.success(body.message || 'Operation successful');
      }
    }),

    catchError((error: HttpErrorResponse) => {
      const apiError = mapApiError(error);
      if (error.status === 403) {
        toast.error('You do not have permission to perform this action');
      } else {
        toast.error(apiError.message);
      }

      console.error('API Error:', apiError);

      return throwError(() => apiError);
    }),
    finalize(() => loadingService.hide()),
  );
};

function mapApiError(error: HttpErrorResponse): ApiError {
  return {
    status: error.error?.status ?? error.status,
    message: extractMessage(error),
    errors: error.error?.errors ?? null,
    code: error.error?.code ?? null,
  };
}

function handleUnauthorized(router: Router, toast: HotToastService) {
  toast.error('Session expired. Please login again.');
  // optional: clear tokens
  localStorage.removeItem('token');
  router.navigate(['/login']);
}

// Helper function to extract error message
function extractMessage(error: HttpErrorResponse): string {
  if (typeof error.error === 'string') return error.error;
  if (error.error?.message) return error.error.message;

  switch (error.status) {
    case 0:
      return 'Network error. Please check your connection.';
    case 400:
      return 'Bad request';
    case 404:
      return 'Resource not found';
    case 500:
      return 'Internal server error';
    default:
      return error.statusText || 'Something went wrong';
  }
}
