import { Component } from '@angular/core';
import {SessionService} from "../../../session.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {DataService} from "../../../data/data.service";

@Component({
  selector: 'app-admin-overview',
  templateUrl: './admin-overview.component.html',
  styleUrls: ['./admin-overview.component.scss']
})
export class AdminOverviewComponent {

  constructor(
    private session: SessionService,
    private router: Router,
    private http: HttpClient,
    private data: DataService
  ) {
    if (!this.session.loggedIn) this.router.navigate(["login"]);
    else {
      setTimeout(() => {
        if (!this.session.user?.isAdmin) {
          alert("You are not an administrator.");
          this.router.navigate([""]);
        }
      }, this.session.user ? 5000 : 0);
    }
  }

  pendingRegistrations = "";
  pendingRegistrationsLoading = false;
  loadPendingRegistrations() {
    this.pendingRegistrationsLoading = true;
    this.http.get<string[]>(this.data.backendRoot + "/admin/pendingRegistrations", this.session.authHeaders).subscribe(pendingRegistrations => {
      this.pendingRegistrations = pendingRegistrations.join("\n");
      this.pendingRegistrationsLoading = false;
    });
  }
}
