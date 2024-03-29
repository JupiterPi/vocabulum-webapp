import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {SessionService} from "../../../session.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = "";
  password = "";

  errorMessage = "";

  constructor(private session: SessionService, private router: Router) {
    this.session.getLoggedIn().subscribe(loggedIn => {
      if (loggedIn) this.navigateProfile();
    });
  }
  private navigateProfile() {
    this.router.navigate(["my"]);
  }

  ready() {
    return this.username != "" && this.password != "";
  }

  login() {
    this.session.login(this.username, this.password)
      .catch(() => this.errorMessage = "Benutzername oder Passwort falsch")
      .then(() => this.navigateProfile());
  }

  signInWithGoogle() {
    this.session.loginGoogle().subscribe(() => this.navigateProfile());
  }

  resetPassword() {
    const email = this.username == "" ? prompt("Gib deine E-Mail-Adresse ein, um das Passwort zurückzusetzen:") : this.username;
    if (email == null) return;
    this.session.resetPassword(email).then(() => {
      alert("Du findest eine E-Mail zum Zurücksetzen deines Passworts in deinem Postfach.");
    });
  }
}
