import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Auth, NewUser, Token } from "app/model/auth";
import { PreloaderService } from "app/preloader/preloader.service";
import { AuthService } from "app/service/auth.service";
import { Http } from "app/service/http.service";
import { environment } from "environments/environment";
import { tap } from "rxjs/operators";

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor(private http: Http, private preloaderService: PreloaderService, private authService: AuthService, private router: Router) {  }

  login(auth: Auth) {
    this.preloaderService.wrap(
      this.http.post<Token>(`${environment.backendPath}/auth`, auth)
        .pipe(
          tap((res) => this.authService.setToken(res)),
          tap(() => {
            this.router.navigate(['projects'], {skipLocationChange: false});
          })
        )
    )
  }

  newUser(newUser: NewUser) {
    this.preloaderService.wrap(
      this.http.post<NewUser>(`${environment.backendPath}/user`, newUser)
    )
  }
}