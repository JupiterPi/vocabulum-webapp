import {Component} from '@angular/core';
import {SessionService} from "../../../session.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
  constructor(public session: SessionService, private router: Router) {
    if (!session.loggedIn) {
      this.router.navigate(["login"]);
    }
  }

  obfuscateEmail(email?: string) {
    if (email == null) return "";
    const atIndex = email.lastIndexOf("@");
    if (atIndex == -1 || atIndex <= 2) return email;
    let str = email.substring(0, 2);
    for (let i = 0; i < atIndex - 2; i++) {
      str += "*";
    }
    return str + email.substring(atIndex);
  }

  showEmail() {
    alert("Deine E-Mail-Adresse lautet: " + this.session.user?.email);
  }

  changePassword() {
    prompt("Gib dein aktuelles Passwort ein:");
    prompt("Gib jetzt ein neues Passwort ein:");
    prompt("Wiederhole das neue Passwort:");
  }
}
