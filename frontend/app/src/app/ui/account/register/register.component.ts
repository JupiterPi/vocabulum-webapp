import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../login_register.scss']
})
export class RegisterComponent {
  username = ""
  email = ""
  password = ""
  passwordRepeat = ""

  passwordRepeatedCorrectly() {
    return this.password == this.passwordRepeat;
  }

  ready() {
    return this.username != "" && this.email != "" && this.password != "" && this.passwordRepeatedCorrectly();
  }
}
