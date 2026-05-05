import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-tooltip-content',
  imports: [],
  templateUrl: './tooltip-content.component.html',
  styleUrl: './tooltip-content.component.css',
  host: {
    '[class]': 'hostClasses()',
  }
})
export class TooltipContentComponent {
  text = input('');
 type = input<string>('');

  // Computed signal to calculate classes based on the type
  protected hostClasses = computed(() => {
    const baseClasses = 'block text-white text-[11px] font-medium px-2 py-1 rounded shadow-md border animate-in fade-in zoom-in duration-150 whitespace-nowrap';


    return `${baseClasses} ${this.type()}`;
  });
}
