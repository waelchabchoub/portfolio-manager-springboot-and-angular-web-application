import { Component, OnInit } from '@angular/core';
import { SkillService } from 'src/app/services/skill.service';
import { Skill } from 'src/app/entities/skill.model'
import { NGXLogger } from 'ngx-logger';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from 'src/app/services/auth.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
	selector: 'app-skills',
	templateUrl: './skills.component.html',
	styleUrls: ['./skills.component.scss']
})
export class SkillsComponent {
	skills: Skill[] = [];
	editedSkill: Skill | null = null;

	constructor(private skillService: SkillService, private logger: NGXLogger, private authService: AuthService, private translate: TranslateService) { }

	ngOnInit(): void {
		this.getSkills();
	}

	getSkills() {
		this.skillService.getSkills().subscribe(
			(response: Skill[]) => {
				if (response) {
					this.skills = response;
				}
			},
			(error) => {
				this.logger.error(error);
			}
		)
	}

	addSkill(skill: string, progress: number) {
		this.skillService.saveSkill({ skill, progress }).subscribe(
			(response: Skill) => {
				this.skills.push(response);
			},
			(error) => {
				this.logger.error(error);
			}
		)
	}

	editSkill() {
		if (this.editedSkill) {
			this.skillService.saveSkill(this.editedSkill).subscribe(
				(response: Skill) => {
					const index = this.skills.findIndex(s => s.id === response.id);
					if (index !== -1) {
						this.skills[index] = response;
					}
					this.editedSkill = null;
				},
				(error) => {
					this.logger.error('Error saving skill:', error);
				}
			);
		}
	}

	deleteSkill(skill: Skill) {
		if (skill.id !== undefined && skill.id !== null) {
			this.skillService.deleteSkill(skill.id).subscribe(
				() => {
					this.skills = this.skills.filter(s => s.id !== skill.id);
				},
				(error) => {
					this.logger.error(error);
				}
			);
		} else {
			this.logger.error('Invalid skill ID. Unable to delete.');
		}
	}

	toggleEdit(skill: Skill) {
		this.editedSkill = { ...skill };
	}

	convertToNumber(value: string): number {
		return parseInt(value, 10) || 0;
	}
	
	isLoggedIn(): boolean {
		return this.authService.isLoggedIn();
	}
}