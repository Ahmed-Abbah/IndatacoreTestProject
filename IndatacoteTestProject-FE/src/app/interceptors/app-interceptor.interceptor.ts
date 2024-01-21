import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class AppInterceptorInterceptor implements HttpInterceptor {

  constructor(private authService: AuthenticationService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if(!request.url.includes("/auth/login") && !request.url.includes("/auth/register")){
      let newReq = request.clone({
        headers : request.headers.set("Authorization","Bearer "+this.authService.accessToken)
      });
    return next.handle(newReq);

    }else return next.handle(request);
  }
}
