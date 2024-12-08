import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Injectable } from "@angular/core";
import { User } from "../entities/user.model";

@Injectable()
export class UserService {
	private apiUrl = '/api/userinfo'; 

	constructor(private http: HttpClient) { }
	
	getUserInfo(): Observable<User> {
		return this.http.get<User>(this.apiUrl.concat("/get"));
	}

	saveUserInfo(user: any): Observable<User> {
		return this.http.post<User>(this.apiUrl.concat("/save"), user);
	}
}