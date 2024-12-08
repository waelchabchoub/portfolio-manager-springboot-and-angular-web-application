import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';


@Component({
	selector: 'app-resume',
	templateUrl: './resume.component.html',
	styleUrls: ['./resume.component.scss']
})
export class ResumeComponent {

	constructor(private translate: TranslateService) { }

}
