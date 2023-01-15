import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SessionService} from "../../../session.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../../../data/data.service";
import {UserDetails} from "../../../data/users.service";

@Component({
  selector: 'app-voucher',
  templateUrl: './voucher.component.html',
  styleUrls: ['./voucher.component.scss']
})
export class VoucherComponent {
  constructor(private data: DataService, private http: HttpClient, private session: SessionService, private route: ActivatedRoute, private router: Router) {
    if (!this.session.loggedIn) {
      alert("Melde dich zuerst an!");
      this.router.navigate(["login"]);
    } else {
      if (this.session.user?.isProUser) {
        alert("Du hast anscheinend schon Pro! Warte, bis dein Abo ausgelaufen ist und versuche es dann erneut.");
        this.router.navigate(["/"]);
      }
    }

    this.route.queryParams.subscribe(queryParams => {
      this.code = queryParams["code"] ?? "";
    });
  }

  code = "";
  success = false;
  failure = false;

  codeValid() {
    return /^[a-zA-Z0-9]+$/.test(this.code) && this.code.length == 8;
  }

  useCode() {
    if (this.codeValid()) {
      this.http.post<UserDetails>(this.data.backendRoot + "/auth/useVoucher/" + this.code, null, this.session.authHeaders).subscribe(user => {
        if (user.isProUser) this.success = true;
        else this.failure = true;
      });
    }
  }

}
