import { Component } from '@angular/core';
import {UserService} from "../../../data/user.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../login_register.scss']
})
export class LoginComponent {
  username = ""
  password = ""

  constructor(private users: UserService) {}

  ready() {
    return this.username != "" && this.password != "";
  }

  login() {
    this.users.login(this.username, this.password).subscribe({
      next: (userDetails) => alert(JSON.stringify(userDetails)),
      error: () => alert("error")
    })
  }
}
