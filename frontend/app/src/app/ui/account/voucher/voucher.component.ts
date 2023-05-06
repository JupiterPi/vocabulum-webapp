import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SessionService} from "../../../session.service";
import {ActivatedRoute, Router} from "@angular/router";
import {CoreService} from "../../../data/core.service";
import {UserDetails} from "../../../data/users.service";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-voucher',
  templateUrl: './voucher.component.html',
  styleUrls: ['./voucher.component.scss']
})
export class VoucherComponent {
  constructor(private data: CoreService, private http: HttpClient, private session: SessionService, private route: ActivatedRoute, private router: Router) {
    this.session.getLoggedIn().subscribe(loggedIn => {
      if (!loggedIn) {
        alert("Melde dich zuerst an!");
        this.router.navigate(["login"]);
      } else {
        if (this.session.user?.isProUser) {
          alert("Du hast anscheinend schon Pro! Warte, bis dein Abo ausgelaufen ist und versuche es dann erneut.");
          this.router.navigate(["/"]);
        }
      }
    });

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
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.post<UserDetails>(environment.apiRoot + "/auth/useVoucher/" + this.code, null, authHeaders).subscribe(user => {
          if (user.isProUser) this.success = true;
          else this.failure = true;
        });
      });
    }
  }

}
