import { Component } from '@angular/core';

type Plan = {
  name: string,
  price: string,
  time: string,
  bullets: string[]
};

@Component({
  selector: 'app-plans',
  templateUrl: './plans.component.html',
  styleUrls: ['./plans.component.scss']
})
export class PlansComponent {
  plans: Plan[] = [
    {
      name: "Probezeit",
      price: "0,99 €",
      time: "2 Wochen",
      bullets: ["kurzer Testzeitraum", "kurzer Testzeitraum", "kurzer Testzeitraum"]
    },
    {
      name: "Monatlich",
      price: "4,99 €",
      time: "3 Monate",
      bullets: ["bester Wert", "bester Wert", "bester Wert"]
    },
    {
      name: "Jährlich",
      price: "19,99 €",
      time: "1 Jahr",
      bullets: ["bester Wert", "bester Wert", "bester Wert"]
    }
  ];

  /*formatPrice(price: number) {
    let a = Math.floor(price).toString();
    let b = Math.floor((price - Math.floor(price)) * 100).toString();
    while (b.length < 2) {
      b = b + "0";
    }
    return a + "," + b;
  }*/
}
