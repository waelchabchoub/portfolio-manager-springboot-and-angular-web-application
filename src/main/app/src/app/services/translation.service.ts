import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
	providedIn: 'root',
})
export class TranslationService {
	private apiKey = 'AIzaSyDVbVkaIr2jl9wmxscpTXVDbdbckQ5-59Y';
	private defaultLanguage: string = 'en';
	private availableLanguages: string[] = ['en'];
	private languagesSubject: BehaviorSubject<string[]> = new BehaviorSubject<string[]>([]);
	private currentLanguageSubject: BehaviorSubject<string> = new BehaviorSubject<string>(this.defaultLanguage);

	constructor(private translate: TranslateService, private http: HttpClient) {
		this.fetchLanguageConfig();
	}

	private fetchLanguageConfig() {
		this.http.get<any>('/api/lang-config').subscribe((config) => {
			this.defaultLanguage = config.defaultLanguage;
			this.availableLanguages = config.availableLanguages;
			this.languagesSubject.next(this.availableLanguages);
			const storedLanguage = localStorage.getItem('language');
			const languageToUse = storedLanguage && this.availableLanguages.includes(storedLanguage)
				? storedLanguage
				: this.defaultLanguage;
			this.loadTranslations(languageToUse);
		});
	}

	loadTranslations(language: string) {
		const basePath = window.location.origin;
		this.http.get<string[]>('/api/translation-files').subscribe((files) => {
			files.forEach((file) => {
				const lang = file.split('-')[1].split('.')[0];

				this.http.get<any>(`/assets/i18n/${file}`).subscribe((translations) => {
					this.translate.setTranslation(lang, translations, true);
				});
			});
			this.translate.use(language);
			localStorage.setItem('language', language);
			this.currentLanguageSubject.next(language);
		});
	}

	switchLanguage(language: string) {
		if (this.availableLanguages.includes(language)) {
			this.loadTranslations(language);
		}
	}

	getAvailableLanguages(): Observable<string[]> {
		return this.languagesSubject.asObservable();
	}

	getCurrentLanguage(): Observable<string> {
		return this.currentLanguageSubject.asObservable();
	}
}