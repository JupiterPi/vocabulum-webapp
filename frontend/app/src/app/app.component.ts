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
        const url = navigationEnd.urlAfterRedirects.substring(1);
        if (url.startsWith("dictionary") || url.startsWith("search")) {
          this.currentSection = "dictionary";
        }
        if (url.startsWith("trainer")) {
          this.currentSection = "trainer";
        }
      }
    });

    /*setInterval(() => {
      this.loggedIn = !this.loggedIn;
    }, 1500);
    //TODO remove*/
  }

  loggedIn = true;
  username = "JupiterPi";
}
