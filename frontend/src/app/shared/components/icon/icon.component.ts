import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-icon',
  imports: [],
  templateUrl: './icon.component.html',
  styleUrl: './icon.component.css',
})
export class IconComponent {
  // Signal Inputs
  name = input.required<string>();
  size = input<string>('1rem'); // Default size is 16px (1rem)

  // Computed path to the sprite
  protected iconPath = computed(() => `assets/icons.svg#${this.name()}`);
}
