import { Component } from '@angular/core';
import {SessionService} from "../../../session.service";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {DataService} from "../../../data/data.service";

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

  pendingRegistrations = new Interface((done, setValue) => {
    this.http.get<string[]>(this.data.backendRoot + "/admin/pendingRegistrations", this.session.authHeaders).subscribe(pendingRegistrations => {
      setValue(pendingRegistrations.join("\n"));
      done();
    });
  });

  reloadUsers = new Interface((done) => {
    this.http.post(this.data.backendRoot + "/admin/reloadUsers", null, this.session.authHeaders).subscribe(() => {
      done();
    });
  });

  reloadHistories = new Interface((done) => {
    this.http.post(this.data.backendRoot + "/admin/reloadHistories", null, this.session.authHeaders).subscribe(() => {
      done();
    });
  });

  vouchers = new Interface((done, setValue) => {
    this.http.get<string[]>(this.data.backendRoot + "/admin/vouchers", this.session.authHeaders).subscribe(vouchers => {
      setValue(vouchers.join("\n"));
      done();
    });
  });

  newVoucherExpirationDate = null;
  newVoucherExpirationTime = "00:00";
  newVoucherAmount = 1;
  newVoucherNote = "";
  createNewVoucher = new Interface((done, setValue) => {
    this.http.post<string[]>(this.data.backendRoot + "/admin/vouchers", {
      expiration: new Date(this.newVoucherExpirationDate + "T" + this.newVoucherExpirationTime).toJSON(),
      amount: this.newVoucherAmount,
      note: this.newVoucherNote
    }, this.session.authHeaders).subscribe(ids => {
      console.log(ids);
      setValue(ids.join("\n"));

      this.newVoucherExpirationDate = null;
      this.newVoucherExpirationTime = "00:00";
      this.newVoucherAmount = 1;
      this.newVoucherNote = "";
      done();
    });
  });

  reloadVouchers = new Interface((done, setValue) => {
    this.http.post(this.data.backendRoot + "/admin/reloadVouchers", null, this.session.authHeaders).subscribe(() => {
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
