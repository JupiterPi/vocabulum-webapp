import { Component } from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";
import {UserService, UserDetails} from "../data/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  currentSection = "";

  constructor(private router: Router, private user: UserService) {
    // navigation
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

    // user details
    this.userDetails = user.getUserDetails();
  }

  loggedIn = true;
  userDetails?: UserDetails;

  authClicked() {
    if (this.loggedIn) {
      this.router.navigate(["my"]);
    } else {
      this.router.navigate(["login"]);
    }
  }
}
