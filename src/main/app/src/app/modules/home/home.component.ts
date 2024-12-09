import { AfterViewInit, Component } from '@angular/core';

import { LibraryInitializationService } from '../../services/library.initialization.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements AfterViewInit {

	constructor(private libraryService: LibraryInitializationService) { }

	ngAfterViewInit(): void {
		this.libraryService.initHeaderToggle();
		this.libraryService.initMobileNav();
		this.libraryService.initPreloader();
		this.libraryService.initScrollTopButton();
		this.libraryService.initAOS();
		this.libraryService.initPureCounter();
		this.libraryService.initSkillsAnimation();
		this.libraryService.initGlightbox();
		this.libraryService.initSwiper();
		this.libraryService.initNavmenuScrollspy();

		setTimeout(() => {
			this.libraryService.initSkillsAnimation();
			const isotopeContainer = document.querySelector('.isotope-container') as HTMLElement;
			if (isotopeContainer) {
				this.libraryService.initIsotope(isotopeContainer);
			}
		}, 200);
	}
}
