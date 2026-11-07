import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-service',
    templateUrl: './service.component.html',
    styleUrls: ['./service.component.scss'],
    standalone: false
})
export class ServiceComponent {
	
	constructor(translate: TranslateService) {}

}
