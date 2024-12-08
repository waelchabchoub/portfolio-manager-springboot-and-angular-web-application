import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { Skill } from "../entities/skill.model";

@Injectable()
export class SkillService {
	private apiUrl = '/api/skill';

	constructor(private http: HttpClient) { }

	getSkills(): Observable<Skill[]> {
		return this.http.get<Skill[]>(this.apiUrl.concat("/get"));
	}

	saveSkill(skill: Skill): Observable<Skill> {
		return this.http.post<Skill>(this.apiUrl.concat("/save"), skill);
	}

	deleteSkill(id: number): Observable<void> {
		return this.http.delete<void>(this.apiUrl.concat("/delete/",id.toString()));
	}
}