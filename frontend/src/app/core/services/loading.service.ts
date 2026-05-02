import { Injectable, signal, computed, inject } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';

@Injectable({ providedIn: 'root' })
export class LoadingService {

  private spinner = inject(NgxSpinnerService);

  private _activeRequests = 0;
  private _isLoading = signal(false);

  public readonly isLoading = computed(() => this._isLoading());

  show() {
    this._activeRequests++;
    if (this._activeRequests === 1) {
      this._isLoading.set(true);
      this.spinner.show();
    }
  }

  hide() {
    this._activeRequests = Math.max(0, this._activeRequests - 1);
    if (this._activeRequests === 0) {
      this._isLoading.set(false);
      this.spinner.hide();
    }
  }
}
