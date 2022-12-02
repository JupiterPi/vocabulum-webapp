import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UsersService} from "../../../data/users.service";

@Component({
  selector: 'app-confirm-email',
  templateUrl: './confirm-email.component.html',
  styleUrls: ['./confirm-email.component.scss']
})
export class ConfirmEmailComponent {
  username = "...";

  showError = false;

  constructor(route: ActivatedRoute, users: UsersService) {
    route.queryParams.subscribe(params => {
      const id = params["id"];
      if (!id) alert("Bitte gib eine ID an!");

      users.confirmRegistration(id).subscribe({
        next: username => {
          this.username = username;
        },
        error: error => {
          this.showError = true;
        }
      });
    });
  }
}
