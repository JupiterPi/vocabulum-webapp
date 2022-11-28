import { Component } from '@angular/core';
import {UsersService} from "../../../data/users.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../login_register.scss']
})
export class RegisterComponent {
  username = "";
  email = "";
  password = "";
  passwordRepeat = "";

  constructor(private users: UsersService, private router: Router) {}

  passwordRepeatedCorrectly() {
    return this.password == this.passwordRepeat;
  }

  ready() {
    return this.username != "" && this.email != "" && this.password != "" && this.passwordRepeatedCorrectly();
  }

  register() {
    this.users.registerNewUser(this.username, this.email, this.password).subscribe(success => {
      this.router.navigate(["login"]);
    });
  }
}
