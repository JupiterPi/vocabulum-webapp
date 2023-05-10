import { Component } from '@angular/core';
import {SessionService} from "../../../session.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {CoreService} from "../../../data/core.service";
import {environment} from "../../../../environments/environment";

class Interface {
  constructor(private handler: (done: () => void, setValue: (value: string) => void) => void) {}
  working = false;
  value = "";
  run = () => {
    this.working = true;
    this.handler(() => {
      this.working = false;
    }, (value) => {
      this.value = value;
    });
  };
}

@Component({
  selector: 'app-admin-overview',
  templateUrl: './admin-overview.component.html',
  styleUrls: ['./admin-overview.component.scss']
})
export class AdminOverviewComponent {

  constructor(
    private session: SessionService,
    private router: Router,
    private http: HttpClient
  ) {
    this.session.getLoggedIn().subscribe(loggedIn => {
      if (!loggedIn) this.router.navigate(["login"]);
      else {
        this.session.isAdmin().subscribe(isAdmin => {
          if (!isAdmin) {
            alert("You are not an administrator.");
            this.router.navigate([""]);
          }
        });
      }
    });

    this.session.getAuthHeaders().subscribe(authHeaders => this.authHeaders = authHeaders);
  }

  authHeaders: any | null = null;

  pendingRegistrations = new Interface((done, setValue) => {
    this.http.get<string[]>(environment.apiRoot + "/admin/pendingRegistrations", {
      observe: "response",
      headers: this.authHeaders.headers,
    }).subscribe(pendingRegistrations => {
      setValue((pendingRegistrations.body ?? []).join("\n"));
      done();
    });
  });

  reloadUsers = new Interface((done) => {
    this.http.post(environment.apiRoot + "/admin/reloadUsers", null, this.authHeaders).subscribe(() => {
      done();
    });
  });

  reloadHistories = new Interface((done) => {
    this.http.post(environment.apiRoot + "/admin/reloadHistories", null, this.authHeaders).subscribe(() => {
      done();
    });
  });

  vouchers = new Interface((done, setValue) => {
    this.http.get<string[]>(environment.apiRoot + "/admin/vouchers", {
      observe: "response",
      headers: this.authHeaders.headers,
    }).subscribe(vouchers => {
      setValue((vouchers.body ?? []).join("\n"));
      done();
    });
  });

  newVoucherExpirationDate = null;
  newVoucherExpirationTime = "00:00";
  newVoucherAmount = 1;
  newVoucherNote = "";
  createNewVoucher = new Interface((done, setValue) => {
    this.http.post<string[]>(environment.apiRoot + "/admin/vouchers", {
      expiration: new Date(this.newVoucherExpirationDate + "T" + this.newVoucherExpirationTime).toJSON(),
      amount: this.newVoucherAmount,
      note: this.newVoucherNote
    }, {
      observe: "response",
      headers: this.authHeaders.headers
    }).subscribe(ids => {
      setValue((ids.body ?? []).join("\n"));

      this.newVoucherExpirationDate = null;
      this.newVoucherExpirationTime = "00:00";
      this.newVoucherAmount = 1;
      this.newVoucherNote = "";
      done();
    });
  });

  reloadVouchers = new Interface((done, setValue) => {
    this.http.post(environment.apiRoot + "/admin/reloadVouchers", null, this.authHeaders).subscribe(() => {
      done();
    });
  });

  printVouchers = "";

  openPrintVouchers() {
    this.router.navigate(["admin", "printVouchers"], {queryParams: {
      vouchers: this.printVouchers
    }});
  }
}
