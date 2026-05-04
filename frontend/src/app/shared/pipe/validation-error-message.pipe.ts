import { Pipe, PipeTransform } from '@angular/core';
import { ValidationErrors } from '@angular/forms';

@Pipe({
  name: 'errorMessage',
  standalone: true,
})
export class ValidationErrorMessagePipe implements PipeTransform {
  // Add fieldName as the second parameter
  transform(errors: ValidationErrors | null | undefined, fieldName: string = 'Field'): string {
    if (!errors) return '';

    const messages: { [key: string]: string } = {
      required: `${fieldName} is mandatory.`, // Now it says "Reason is mandatory"
      maxlength: `${fieldName} exceeds max length (${errors['maxlength']?.requiredLength}).`,
      dateRangeInvalid: 'The end date must be after the start date.',
      email: 'Please provide a valid email.',
    };

    const firstKey = Object.keys(errors)[0];
    return messages[firstKey] || `${fieldName} is invalid.`;
  }
}
