import { Component } from '@angular/core';
import {SessionService} from "../../../session.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-overview',
  templateUrl: './admin-overview.component.html',
  styleUrls: ['./admin-overview.component.scss']
})
export class AdminOverviewComponent {

  constructor(
    private session: SessionService,
    private router: Router
  ) {
    if (!this.session.loggedIn) this.router.navigate(["login"]);
    else {
      setTimeout(() => {
        if (!this.session.user?.isAdmin) {
          alert("You are not an administrator.");
          this.router.navigate([""]);
        }
      }, this.session.user ? 3000 : 0);
    }
  }
}
