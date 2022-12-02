import {Component} from '@angular/core';
import {SessionService} from "../../../session.service";
import {Router} from "@angular/router";
import {UsersService} from "../../../data/users.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
  constructor(public session: SessionService, private router: Router, private users: UsersService) {
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

  changeUsername() {
    const newUsername = prompt("Gib einen neuen Nutzernamen ein:");
    if (newUsername != null) this.users.changeUsername(newUsername).subscribe(userDetails => {
      this.session.user = userDetails;
    });
  }

  changePassword() {
    const password = prompt("Gib dein aktuelles Passwort ein:");
    const newPassword = prompt("Gib jetzt ein neues Passwort ein:");

    if (prompt("Wiederhole das neue Passwort:") != newPassword) {
      alert("Stimmt nicht überein!");
      return;
    }

    if (password == null || newPassword == null) return;

    if (this.session.user == null) return;
    this.users.verifyCredentials(this.session.user?.username, password).subscribe(verification => {
      if (verification.valid) {
        this.users.changePassword(newPassword).subscribe();
      } else {
        alert("Falsches Passwort!");
      }
    });
  }
}
