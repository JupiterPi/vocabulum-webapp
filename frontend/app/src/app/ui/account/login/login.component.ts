import {Component} from '@angular/core';
import {Auth, signInWithEmailAndPassword} from "@angular/fire/auth";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../login_register.scss']
})
export class LoginComponent {
  username = "";
  password = "";

  errorMessage = "";

  constructor(private auth: Auth, private router: Router) {}

  ready() {
    return this.username != "" && this.password != "";
  }

  login() {
    signInWithEmailAndPassword(this.auth, this.username, this.password)
      .catch(_ => this.errorMessage = "Benutzername oder Passwort falsch")
      .then(user => {
        console.log(user);
        this.router.navigate(["my"]);
      });
  }
}
