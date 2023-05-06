import {Component} from '@angular/core';
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

  constructor(private session: SessionService, private router: Router) {
    this.session.loggedIn$.subscribe(loggedIn => {
      if (loggedIn) this.router.navigate(["my"]);
    });
  }

  ready() {
    return this.username != "" && this.password != "";
  }

  login() {
    this.session.login(this.username, this.password)
      .catch(_ => this.errorMessage = "Benutzername oder Passwort falsch");
  }
}
