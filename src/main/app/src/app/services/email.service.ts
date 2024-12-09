import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root',
})
export class EmailService {

	private apiUrl = '/api/emails/send';

	constructor(private http: HttpClient) { }

	sendEmail(formData: { name: string, email: string, subject: string, message: string }): Observable<any> {
		const params = new HttpParams()
			.set('name', formData.name)
			.set('email', formData.email)
			.set('subject', formData.subject)
			.set('message', formData.message);

		return this.http.post<any>(this.apiUrl, null, { params });
	}
}