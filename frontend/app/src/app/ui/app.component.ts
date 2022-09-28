import { Component } from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  currentSection = "";

  constructor(private router: Router) {
    router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const navigationEnd = event as NavigationEnd;
        const url = navigationEnd.urlAfterRedirects.substring(1);
        if (url.startsWith("dictionary") || url.startsWith("search")) {
          this.currentSection = "dictionary";
        } else if (url.startsWith("trainer")) {
          this.currentSection = "trainer";
        } else if (url.startsWith("translationAssistance")) {
          this.currentSection = "translationAssistance";
        } else {
          this.currentSection = "";
        }
      }
    });
  }

  loggedIn = false;
  username = "JupiterPi";

  authClicked() {
    if (this.loggedIn) {
      alert("showing user profile");
    } else {
      this.router.navigate(["login"]);
    }
  }
}
