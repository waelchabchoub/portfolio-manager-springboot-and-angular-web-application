import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ContactForm {
	name: string;
	email: string;
	subject: string;
	message: string;
	/** Honeypot: real users never fill it in. */
	website: string;
}

@Injectable({
	providedIn: 'root',
})
export class EmailService {

	private apiUrl = '/api/emails/send';

	constructor(private http: HttpClient) { }

	sendEmail(formData: ContactForm): Observable<any> {
		return this.http.post<any>(this.apiUrl, formData);
	}
}
