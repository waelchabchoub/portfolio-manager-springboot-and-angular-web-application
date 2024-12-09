import { Injectable } from '@angular/core';

declare var AOS: any;
declare var Typed: any;
declare var GLightbox: any;
declare var PureCounter: any;
declare var Waypoint: any;
declare var Isotope: any;
declare var Swiper: any;
declare var imagesLoaded: any;

@Injectable({
	providedIn: 'root'
})
export class LibraryInitializationService {

	constructor() { }

	public initHeaderToggle(): void {
		const headerToggleBtn = document.querySelector('.header-toggle') as HTMLElement;
		if (headerToggleBtn) {
			headerToggleBtn.addEventListener('click', () => {
				document.querySelector('#header')?.classList.toggle('header-show');
				headerToggleBtn.classList.toggle('bi-list');
				headerToggleBtn.classList.toggle('bi-x');
			});
		}
	}

	public initMobileNav(): void {
		document.querySelectorAll('#navmenu a').forEach(navmenu => {
			navmenu.addEventListener('click', () => {
				const headerToggleBtn = document.querySelector('.header-toggle') as HTMLElement;
				if (headerToggleBtn && document.querySelector('.header-show')) {
					headerToggleBtn.click();
				}
			});
		});
	}

	public initPreloader(): void {
		const preloader = document.querySelector('#preloader');
		if (preloader) {
			window.addEventListener('load', () => {
				preloader.remove();
			});
		}
	}

	public initScrollTopButton(): void {
		const scrollTop = document.querySelector('.scroll-top') as HTMLElement;
		if (scrollTop) {
			window.addEventListener('scroll', () => {
				window.scrollY > 100 ? scrollTop.classList.add('active') : scrollTop.classList.remove('active');
			});

			scrollTop.addEventListener('click', (e) => {
				e.preventDefault();
				window.scrollTo({
					top: 0,
					behavior: 'smooth'
				});
			});
		}
	}

	public initAOS(): void {
		window.addEventListener('load', () => {
			AOS.init({
				duration: 600,
				easing: 'ease-in-out',
				once: true,
				mirror: false
			});
		});
	}

	public initTypedJS(): void {
		document.addEventListener('DOMContentLoaded', function() {
			const selectTyped = document.querySelector('.typed') as HTMLElement;
			if (selectTyped) {
				const typedStrings = selectTyped.getAttribute('data-typed-items')?.split(',');
				if (typedStrings) {
					new Typed('.typed', {
						strings: typedStrings,
						loop: true,
						typeSpeed: 100,
						backSpeed: 50,
						backDelay: 2000,
						showCursor: true,
						cursorChar: "|",
					});
				}
			}
		});
	}

	public initPureCounter(): void {
		new PureCounter();
	}

	public initSkillsAnimation(): void {
		const progressBars = document.querySelectorAll('.progress .progress-bar') as NodeListOf<HTMLElement>;
		progressBars.forEach((progressBar) => {
			progressBar.style.width = '0%';
			progressBar.style.transition = 'none';
		});
		const skillsElements = document.querySelectorAll('.skills-animation');
		skillsElements.forEach((item) => {
			new Waypoint({
				element: item as HTMLElement,
				offset: '80%',
				handler: function() {
					const progressBars = item.querySelectorAll('.progress .progress-bar') as NodeListOf<HTMLElement>;
					progressBars.forEach((progressBar) => {
						const value = progressBar.getAttribute('aria-valuenow');
						if (value) {
							setTimeout(() => {
								progressBar.style.transition = 'width 1s ease-out';
								progressBar.style.width = `${value}%`;
							}, 100);
						}
					});
				},
			});
		});
	}

	public initGlightbox(): void {
		GLightbox({
			selector: '.glightbox'
		});
	}

	public initIsotope(isotopeContainer: HTMLElement): void {
		let initIsotope;
		imagesLoaded(isotopeContainer.querySelector('.isotope-container'), () => {
			initIsotope = new Isotope(isotopeContainer.querySelector('.isotope-container') as Element, {
				itemSelector: '.isotope-item',
				layoutMode: 'masonry',
				filter: '*',
				sortBy: 'original-order',
			});
		});

		this.initIsotopeFilters(isotopeContainer, initIsotope);
	}

	public initIsotopeFilters(isotopeItem: HTMLElement, initIsotope: any): void {
		const filters = isotopeItem.querySelectorAll('.isotope-filters li') as NodeListOf<HTMLLIElement>;

		filters.forEach((filter) => {
			filter.addEventListener('click', function(this: HTMLLIElement) {
				const activeFilter = isotopeItem.querySelector('.isotope-filters .filter-active') as HTMLLIElement;
				activeFilter?.classList.remove('filter-active');
				this.classList.add('filter-active');

				initIsotope.arrange({
					filter: this.getAttribute('data-filter') ?? '*',
				});

				if (typeof (this as any).initAOS === 'function') {
					(this as any).initAOS();
				}
			});
		});
	}

	public initSwiper(): void {
		document.querySelectorAll(".init-swiper").forEach((swiperElement) => {
			const config = JSON.parse(
				swiperElement.querySelector(".swiper-config")!.innerHTML.trim()
			);

			if (swiperElement.classList.contains("swiper-tab")) {
				this.initSwiperWithCustomPagination(swiperElement, config);
			} else {
				new Swiper(swiperElement, config);
			}
		});
	}

	public initSwiperWithCustomPagination(swiperElement: any, config: any): void {
		new Swiper(swiperElement, config);
	}

	public initNavmenuScrollspy(): void {
		const navmenulinks = document.querySelectorAll('.navmenu a') as NodeListOf<HTMLAnchorElement>;

		window.addEventListener('load', () => {
			this.navmenuScrollspy(navmenulinks);
		});

		document.addEventListener('scroll', () => {
			this.navmenuScrollspy(navmenulinks);
		});
	}

	public navmenuScrollspy(navmenulinks: NodeListOf<HTMLAnchorElement>): void {
		navmenulinks.forEach((navmenulink) => {
			if (!navmenulink.hash) return;
			const section = document.querySelector(navmenulink.hash) as HTMLElement;
			if (!section) return;

			const position = window.scrollY + 200;
			if (position >= section.offsetTop && position <= (section.offsetTop + section.offsetHeight)) {
				document.querySelectorAll('.navmenu a.active').forEach(link => link.classList.remove('active'));
				navmenulink.classList.add('active');
			} else {
				navmenulink.classList.remove('active');
			}
		});
	}
}