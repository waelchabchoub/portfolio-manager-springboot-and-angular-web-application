import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
	selector: 'app-admin',
	templateUrl: './admin.component.html',
	styleUrls: ['./admin.component.scss']
})
export class AdminComponent {
	username = '';
	password = '';
	errorMessage = '';

	constructor(private authService: AuthService, private router: Router) { }

	login(): void {
		const credentials = {
			username: this.username,
			password: this.password
		};
		this.authService.login(credentials).subscribe(
			(response) => {
				this.authService.saveToken(response.token);
				this.router.navigate(['']);
			},
			(error) => {
				this.errorMessage = 'Invalid credentials or server error!';
			}
		);
	}

}
