import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-stats',
    templateUrl: './stats.component.html',
    styleUrls: ['./stats.component.scss'],
    standalone: false
})
export class StatsComponent {

	constructor(private translate: TranslateService) { }

}
