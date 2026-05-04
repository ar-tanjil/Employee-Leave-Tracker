import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'charCount',
  standalone: true
})
export class CharCountPipe implements PipeTransform {
  // Takes a string and returns the length, or 0 if empty
  transform(value: string | null | undefined): number {
    return value ? value.length : 0;
  }
}