import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-voucher-terminal',
  templateUrl: './voucher-terminal.component.html',
  styleUrls: ['./voucher-terminal.component.scss']
})
export class VoucherTerminalComponent {
  code?: string;

  constructor(private route: ActivatedRoute) {
    this.route.queryParams.subscribe(queryParams => {
      this.code = queryParams["code"];
    });
  }
}
