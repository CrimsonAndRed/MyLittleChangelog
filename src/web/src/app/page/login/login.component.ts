import { OnInit } from "@angular/core";
import { Component } from "@angular/core";
import { LoginService } from "./login.service";

@Component({
    selector: 'login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
  })
  export class LoginComponent {

    constructor(private loginService: LoginService) { }

    login = '';
    password = '';


    onLoginClick(): void {
      this.loginService.login({
          login: this.login,
          password: this.password
      });
    }
  }