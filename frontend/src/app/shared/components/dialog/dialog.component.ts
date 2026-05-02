import { Component, computed, inject, input, output } from '@angular/core';
import { LoadingService } from '../../../core/services/loading.service';

@Component({
  selector: 'app-dialog',
  imports: [],
  templateUrl: './dialog.component.html',
  styleUrl: './dialog.component.css',
})
export class DialogComponent {

  // inject
  private readonly loadingService = inject(LoadingService);

  // input
  readonly title = input.required<string>();

  // output
  readonly close = output<void>();

  // state
  readonly loading = computed(() => this.loadingService.isLoading());

  onBackdropClick(event: MouseEvent): void {
    if ((event.target as HTMLElement) === event.currentTarget) {
      this.close.emit();
    }
  }
}
