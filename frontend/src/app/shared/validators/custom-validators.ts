import { AbstractControl, ValidationErrors, ValidatorFn, FormGroup } from '@angular/forms';

export class CustomValidators {
  static dateRange(startControlName: string, endControlName: string): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const group = control as FormGroup;
      const start = group.get(startControlName)?.value;
      const end = group.get(endControlName)?.value;

      if (start && end && new Date(end) < new Date(start)) {
        return { dateRangeInvalid: true };
      }
      return null;
    };
  }
}