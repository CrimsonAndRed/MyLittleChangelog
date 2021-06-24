import { Injectable } from "@angular/core";
import { Token } from "app/model/auth";
import { StorageServiceModule  } from 'ngx-webstorage-service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

  setToken(token: Token) {
    localStorage.setItem("token", token.token);
  }
}