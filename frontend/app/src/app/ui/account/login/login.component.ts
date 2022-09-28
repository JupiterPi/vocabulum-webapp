import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../login_register.css']
})
export class LoginComponent {
  username = ""
  password = ""

  ready() {
    return this.username != "" && this.password != "";
  }
}
