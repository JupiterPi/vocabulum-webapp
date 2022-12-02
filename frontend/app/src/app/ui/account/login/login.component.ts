import { Component } from '@angular/core';
import {UsersService} from "../../../data/users.service";
import {Router} from "@angular/router";
import {SessionService} from "../../../session.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../login_register.scss']
})
export class LoginComponent {
  username = "";
  password = "";

  errorMessage = "";

  constructor(private users: UsersService, private session: SessionService, private router: Router) {}

  ready() {
    return this.username != "" && this.password != "";
  }

  login() {
    this.errorMessage = "";
    this.users.verifyCredentials(this.username, this.password).subscribe(verification => {
      if (verification.valid) {
        this.users.login(verification.email, this.password).subscribe(userDetails => {
          this.users.authHeaders = this.users.makeAuthHeaders(verification.email, this.password);
          this.session.login(userDetails);
          this.router.navigate(["my"]);
        });
      } else {
        this.errorMessage = "Benutzername oder Passwort falsch";
      }
    });
  }
}
