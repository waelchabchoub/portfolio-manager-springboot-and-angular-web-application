import { Component, OnInit } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/entities/user.model'
import { AuthService } from 'src/app/services/auth.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-about',
	templateUrl: './about.component.html',
	styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {
	user: User = {} as User;
	isEditing = false;

	constructor(private userService: UserService, private logger: NGXLogger, private authService:AuthService, private translate: TranslateService) {}

	ngOnInit(): void {
		this.getUserInfo();
	}

	getUserInfo() {
		this.userService.getUserInfo().subscribe(
			(response) => {
				if (response) {
					this.user = response;
				}
			},
			(error) => {
				this.logger.error(error);
			}
		)
	}

	saveChanges() {
		this.userService.saveUserInfo(this.user).subscribe(

			(response) => {
				this.user = response;
				this.toggleEdit();
			},
			(error) => {
				this.logger.error(error);
			}
		)
	}

	toggleEdit() {
		this.isEditing = !this.isEditing;
	}
	
	isLoggedIn(): boolean {
		return this.authService.isLoggedIn();
	}
}
