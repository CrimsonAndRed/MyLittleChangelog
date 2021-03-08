import { OnInit } from "@angular/core";
import { Component } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { LoginService } from "./login.service";
import { NewUserModalComponent } from "./new-user-modal/new-user-modal.component";

@Component({
    selector: 'login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
  })
  export class LoginComponent {

    constructor(private loginService: LoginService, private dialog: MatDialog) { }

    login = '';
    password = '';


    onLoginClick(): void {
      this.loginService.login({
          login: this.login,
          password: this.password
      });
    }

    onNewUserClick(): void {

      const dialogRef = this.dialog.open(NewUserModalComponent, {
        hasBackdrop: true
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.loginService.newUser(result);
        }
      });
    }
  }