import {
  AbstractControl,
  ValidationErrors,
  ValidatorFn
} from '@angular/forms';

// Validates new and confirm password
export function passwordMatchValidator(): ValidatorFn {

  return (
    control: AbstractControl
  ): ValidationErrors | null => {

    const newPassword =
      control.get('newPassword')?.value;

    const confirmPassword =
      control.get('confirmPassword')?.value;

    // Don't show mismatch until both fields have values
    if (!newPassword || !confirmPassword) {
      return null;
    }

    return newPassword === confirmPassword
      ? null
      : {
          passwordMismatch: true
        };

  };

}