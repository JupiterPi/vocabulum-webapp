import { Component } from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  currentSection = "";

  constructor(router: Router) {
    router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const navigationEnd = event as NavigationEnd;
        const url = navigationEnd.url.substring(1);
        if (url.startsWith("dictionary") || url.startsWith("search")) {
          this.currentSection = "dictionary";
        }
        if (url.startsWith("trainer")) {
          this.currentSection = "trainer";
        }
      }
    });
  }
}
