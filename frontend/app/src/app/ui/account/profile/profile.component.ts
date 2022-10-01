import { Component } from '@angular/core';
import {UserService, UserDetails} from "../../../data/user.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
  constructor(private user: UserService) {
    this.userDetails = user.getUserDetails();
  }

  userDetails: UserDetails = {
    username: "...",
    email: "..."
  };

  obfuscateEmail(email: string) {
    const atIndex = email.lastIndexOf("@");
    if (atIndex == -1 || atIndex <= 2) return email;
    let str = email.substring(0, 2);
    for (let i = 0; i < atIndex - 2; i++) {
      str += "*";
    }
    return str + email.substring(atIndex);
  }

  showEmail() {
    alert("Deine E-Mail-Adresse lautet: " + this.userDetails.email);
  }

  changePassword() {
    prompt("Gib dein aktuelles Passwort ein:");
    prompt("Gib jetzt ein neues Passwort ein:");
    prompt("Wiederhole das neue Passwort:");
  }
}
