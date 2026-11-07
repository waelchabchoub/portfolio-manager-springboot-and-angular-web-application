import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { EmailService } from 'src/app/services/email.service';

@Component({
    selector: 'app-contact',
    templateUrl: './contact.component.html',
    styleUrls: ['./contact.component.scss'],
    standalone: false
})
export class ContactComponent {
	formData = {
		name: '',
		email: '',
		subject: '',
		message: '',
		website: ''
	};

	/** i18n key of the error returned by the server, if any. */
	serverErrorMessage: string = '';
	formErrorMessage: boolean = false;
	successMessage: boolean = false;
	isLoading: boolean = false;

	constructor(translate: TranslateService, private emailService: EmailService) { }

	sendEmail() {
		if (this.formData.name && this.formData.email && this.formData.subject && this.formData.message) {
			this.formErrorMessage = false;
			this.successMessage = false;
			this.serverErrorMessage = '';
			this.isLoading = true;
			this.emailService.sendEmail(this.formData).subscribe(
				(response) => {
					this.isLoading = false;
					this.successMessage = true;
					this.resetFormData();
				},
				(error: HttpErrorResponse) => {
					this.isLoading = false;
					this.serverErrorMessage = error.status === 429 ? 'CONTACT.TOOMANY'
						: error.status === 400 ? 'CONTACT.INVALID' : 'CONTACT.SERVERERROR';
				}
			);
		} else {
			this.formErrorMessage = true;
		}
	}

	resetFormData() {
		this.formData = {
			name: '',
			email: '',
			subject: '',
			message: '',
			website: ''
		};
	}
}
