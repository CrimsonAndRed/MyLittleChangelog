import { Component } from "@angular/core";
import { NewUser } from "app/model/auth";

@Component({
    selector: 'new-user-modal',
    templateUrl: './new-user-modal.component.html',
    styleUrls: ['./new-user-modal.component.scss']
  })
  export class NewUserModalComponent {

    constructor() { }

    newUser: NewUser = {
        login: '',
        password: ''
    }
  }