import {Component, ElementRef, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

export type Voucher = {
  code: string,
  title: string
}

@Component({
  selector: 'app-vouchers-printable',
  templateUrl: './vouchers-printable.component.html',
  styleUrls: ['./vouchers-printable.component.scss']
})
export class VouchersPrintableComponent {
  constructor(private route: ActivatedRoute, private router: Router) {
    this.route.queryParams.subscribe(queryParams => {
      const vouchersStr = queryParams["vouchers"];
      if (vouchersStr) this.vouchersStr = vouchersStr;
    });
  }

  navigateBack() {
    this.router.navigate(["admin"]);
  }

  vouchersStr = "";

  makeVouchers(input: string) {
    const lines = input.split("\n").filter(line => line.length > 0);
    const vouchers = lines.map(line => {
      const parts = line.split(":");
      return {
        code: parts[0],
        title: parts.length > 1 ? parts[1] : "Note"
      }
    });
    if (false) { // repeat for more vouchers
      for (let i = 0; i < 3; i++) {
        const moreVouchers = [];
        for (let voucher of vouchers) {
          moreVouchers.push(voucher);
        }
        moreVouchers.forEach(voucher => vouchers.push(voucher));
      }
    }
    return vouchers;
  }

  title = "Gutschein für // %";
  calculateTitle(voucher: Voucher) {
    let title = this.title.replace("%", voucher.title);
    return title.split(" // ");
  }

  expiration = "";
  hasExpiration() {
    return this.expiration != "";
  }

  showHelpLines = false;
}
