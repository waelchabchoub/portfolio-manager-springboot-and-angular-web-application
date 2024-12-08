import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { EmailService } from 'src/app/services/email.service';

@Component({
	selector: 'app-contact',
	templateUrl: './contact.component.html',
	styleUrls: ['./contact.component.scss']
})
export class ContactComponent {
	formData = {
		name: '',
		email: '',
		subject: '',
		message: ''
	};

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
				},
				(error) => {
					this.isLoading = false;
					if (error.status == 200) {
						this.successMessage = true;
					} else {
						this.serverErrorMessage = error.message;
					}
				}
			);
		} else {
			this.formErrorMessage = true;
		}
	}

}
