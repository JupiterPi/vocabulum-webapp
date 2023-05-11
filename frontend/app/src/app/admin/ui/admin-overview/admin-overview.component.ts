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

  terminalName = "";
  terminalExpirationDate = "";
  terminalExpirationTime = "00:00";
  setTerminalExpiration(interval?: number) {
    if (!interval) interval = parseInt(prompt("Interval:") ?? "0");
    let date = new Date();
    date.setDate(date.getDate() + interval);
    this.terminalExpirationDate = date.toISOString().slice(0, 10);
    this.terminalExpirationTime = "23:59";
  }
  openTerminal() {
    this.http.post<string[]>(environment.apiRoot + "/admin/vouchers", {
      expiration: new Date(this.terminalExpirationDate + "T" + this.terminalExpirationTime).toJSON(),
      amount: 1,
      note: "via Terminal: " + this.terminalName
    }, {
      observe: "response",
      headers: this.authHeaders.headers
    }).subscribe(ids => {
      const id = (ids.body as string[])[0];
      this.router.navigate(["admin", "terminal"], { queryParams: {
        code: id
      }});
    });
  }

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

  printVouchers = "";

  openPrintVouchers() {
    this.router.navigate(["admin", "printVouchers"], {queryParams: {
      vouchers: this.printVouchers
    }});
  }
}
