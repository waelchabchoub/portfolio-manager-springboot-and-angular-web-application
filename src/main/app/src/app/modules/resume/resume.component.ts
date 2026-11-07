import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';


@Component({
    selector: 'app-resume',
    templateUrl: './resume.component.html',
    styleUrls: ['./resume.component.scss'],
    standalone: false
})
export class ResumeComponent {

	constructor(private translate: TranslateService) { }

	get cvUrl(): string {
		const lang = (this.translate.currentLang || this.translate.defaultLang) === 'de' ? 'de' : 'en';
		return `/assets/cv/Wael_Chabchoub_CV_${lang}.pdf`;
	}

}
