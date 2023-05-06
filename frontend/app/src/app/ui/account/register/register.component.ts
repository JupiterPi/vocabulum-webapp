import {Component} from '@angular/core';
import {UsersService} from "../../../data/users.service";

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

  constructor(private users: UsersService) {}

  passwordRepeatedCorrectly() {
    return this.password == this.passwordRepeat;
  }

  ready() {
    return this.username != "" && this.email != "" && this.password != "" && this.passwordRepeatedCorrectly();
  }

  register() {
    alert("Registrierungen sind derzeit in Arbeit... Bitte kontaktiere uns unter support@vocabulum.de");
    /*if (this.success) return;
    this.users.registerNewUser(this.username, this.email, this.password).subscribe(() => {
      this.success = true;
    });*/
  }
}
