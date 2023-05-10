import {Component} from '@angular/core';
import {SessionService} from "../../../session.service";

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

  success = false;

  constructor(private session: SessionService) {}

  passwordRepeatedCorrectly() {
    return this.password == this.passwordRepeat;
  }

  ready() {
    return this.username != "" && this.email != "" && this.password != "" && this.passwordRepeatedCorrectly();
  }

  register() {
    if (this.success) return;
    this.session.createAccountAndLogin(this.username, this.email, this.password).subscribe(() => {
      this.success = true;
    });
  }
}
