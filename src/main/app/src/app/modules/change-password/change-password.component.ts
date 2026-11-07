import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';

@Component({
	selector: 'app-change-password',
	templateUrl: './change-password.component.html',
	styleUrls: ['./change-password.component.scss'],
	standalone: false
})
export class ChangePasswordComponent {
	readonly minLength = 8;
	currentPassword = '';
	newPassword = '';
	confirmPassword = '';
	errorMessage = '';
	successMessage = '';
	submitting = false;

	constructor(private authService: AuthService, private router: Router) { }

	submit(): void {
		this.errorMessage = '';
		if (this.newPassword.length < this.minLength) {
			this.errorMessage = `The new password must have at least ${this.minLength} characters.`;
			return;
		}
		if (this.newPassword !== this.confirmPassword) {
			this.errorMessage = 'The new passwords do not match.';
			return;
		}
		if (this.newPassword === this.currentPassword) {
			this.errorMessage = 'The new password must be different from the current one.';
			return;
		}

		this.submitting = true;
		this.authService.changePassword(this.currentPassword, this.newPassword).subscribe({
			next: () => {
				this.successMessage = 'Password changed. Please log in again.';
				setTimeout(() => {
					this.authService.logout();
					this.router.navigate(['/admin']);
				}, 1500);
			},
			error: (error: HttpErrorResponse) => {
				this.submitting = false;
				if (error.status === 403 && error.error?.message === 'WRONG_CURRENT_PASSWORD') {
					this.errorMessage = 'The current password is incorrect.';
				} else if (error.status === 401 || error.status === 403) {
					this.errorMessage = 'Your session has expired. Please log in again.';
				} else {
					this.errorMessage = 'Could not change the password. Please try again.';
				}
			}
		});
	}

	cancel(): void {
		this.router.navigate(['/home']);
	}
}
