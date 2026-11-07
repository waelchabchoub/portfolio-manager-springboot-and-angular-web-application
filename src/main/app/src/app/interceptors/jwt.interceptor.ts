import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.authService.getToken();
    const request = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // The token expired or was rejected: log out. A wrong password on /auth/login is also a 401, so skip it.
        if (token && error.status === 401 && !req.url.endsWith('/auth/login')) {
          this.authService.logout();
          this.router.navigate(['/admin']);
        }
        return throwError(() => error);
      })
    );
  }
}
