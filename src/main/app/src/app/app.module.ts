import { NgModule } from '@angular/core'; import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { AppComponent } from './app.component';
import { HomeComponent } from './modules/home/home.component';
import { HeaderComponent } from './modules/header/header.component';
import { HeroComponent } from './modules/hero/hero.component';
import { AboutComponent } from './modules/about/about.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { UserService } from './services/user.service';
import { AdminComponent } from './modules/admin/admin.component';
import { AppRoutingModule } from './app-routing.module';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { RouterModule, Routes } from '@angular/router';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { LogoutComponent } from './modules/logout/logout.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslationService } from './services/translation.service';
import { LanguageSelectionComponent } from './modules/language-selection/language-selection.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MdbDropdownModule } from 'mdb-angular-ui-kit/dropdown';
import { StatsComponent } from './modules/stats/stats.component';
import { LibraryInitializationService } from './services/library.initialization.service';
import { SkillsComponent } from './modules/skills/skills.component';
import { SkillService } from './services/skill.service';
import { ResumeComponent } from './modules/resume/resume.component';
import { ServiceComponent } from './modules/service/service.component';
import { ContactComponent } from './modules/contact/contact.component';
import { FooterComponent } from './modules/footer/footer.component';
import { EmailService } from './services/email.service';

export function HttpLoaderFactory(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		HeaderComponent,
		HeroComponent,
		AboutComponent,
		AdminComponent,
		LogoutComponent,
		LanguageSelectionComponent,
		StatsComponent,
		SkillsComponent,
  ResumeComponent,
  ServiceComponent,
  ContactComponent,
  FooterComponent,
	],
	imports: [
		CommonModule,
		BrowserModule,
		HttpClientModule,
		FormsModule,
		AppRoutingModule,
		LoggerModule.forRoot({
			level: NgxLoggerLevel.DEBUG,
			serverLogLevel: NgxLoggerLevel.ERROR,
		}),
		RouterModule.forRoot([], {
			scrollPositionRestoration: 'enabled',
			anchorScrolling: 'enabled'
		}),
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: HttpLoaderFactory,
				deps: [HttpClient]
			}
		}),
		BrowserAnimationsModule,
		MdbDropdownModule
	],
	providers: [
		UserService,
		{
			provide: HTTP_INTERCEPTORS,
			useClass: JwtInterceptor,
			multi: true
		},
		TranslationService,
		LibraryInitializationService,
		SkillService,
		EmailService
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
