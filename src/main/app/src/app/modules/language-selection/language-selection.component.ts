import { Component, OnInit } from '@angular/core';
import { TranslationService } from '../../services/translation.service';

@Component({
	selector: 'app-language-selection',
	templateUrl: './language-selection.component.html',
	styleUrls: ['./language-selection.component.scss']
})
export class LanguageSelectionComponent implements OnInit {
	availableLanguages: string[] = [];
	selectedLanguage: string = 'en';
	originalText = "this is an example";

	constructor(private translationService: TranslationService) { }

	ngOnInit() {

		this.translationService.getAvailableLanguages().subscribe((languages) => {
			this.availableLanguages = languages;
		});

		this.translationService.getCurrentLanguage().subscribe((language) => {
			this.selectedLanguage = language;
		});
	}

	onLanguageChange(language: string) {
		this.translationService.switchLanguage(language);
		this.selectedLanguage = language;
	}

}
