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
    this.session.loggedIn$.subscribe(loggedIn => {
      if (!loggedIn) {
        this.router.navigate(["login"]);
      }
    });
  }

  isAdmin = this.session.isAdmin()

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

  obfuscateDiscordUsername(username?: string) {
    if (username == null) return "";
    const name = username.substring(0, username.lastIndexOf("#"));
    if (name.length <= 3) return name + "#****";
    let str = name.substring(0, 3);
    for (let i = 3; i < name.length; i++) {
      str += "*";
    }
    return str + "#****";
  }

  showEmail() {
    alert("Deine E-Mail-Adresse lautet: " + this.session.user?.email);
  }

  showDiscordUsername() {
    alert("Dein Discord-Benutzername lautet: " + this.session.userDetails?.discordUsername);
  }

  changeUsername() {
    const newUsername = prompt("Gib einen neuen Nutzernamen ein:");
    if (newUsername != null) this.users.changeUsername(newUsername).subscribe(userDetails => this.session.setUserDetails(userDetails));
  }

  showPasswordReset() {
    return this.session.user?.displayName == null;
    //TODO add proper logic (via provider type)
  }
  resetPassword() {
    this.session.resetPassword().then(() => {
      alert("Du findest eine E-Mail zum Zurücksetzen deines Passworts in deinem Postfach.");
    });
  }

  changeDiscordUsername() {
    const newUsername = prompt("Gib deinen Discord-Benutzernamen ein: (z. B. \"MeinName#1234\")");
    if (newUsername != null) {
      if (newUsername.match(/^.{3,32}#[0-9]{4}$/g) == null) {
        alert("Bitte gib einen korrekten Discord-Benutzernamen wie \"MeinName#1234\" ein.");
      } else {
        this.users.changeDiscordUsername(newUsername).subscribe(userDetails => this.session.setUserDetails(userDetails));
      }
    }
  }

  clearHistory() {
    this.users.clearHistory().subscribe(_ => {
      alert("Bitte lade die Seite neu, um die Aktualisierung zu laden.");
    });
  }

  logout() {
    this.session.logout();
    this.router.navigate(["login"]);
  }

  openAdminConsole() {
    this.router.navigate(["admin"]);
  }
}
