import {Component} from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";
import {UsersService} from "../data/users.service";
import {SessionService} from "../session.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  currentSection = "";

  constructor(private router: Router, private user: UsersService, public session: SessionService) {
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

        [50, 200, 500, 1000, 2*1000, 3*1000, 5*1000].forEach(timeout => {
          setTimeout(() => {
            this.refreshScrollbarVisible();
          }, timeout);
        });
      }
    });
  }

  authClicked() {
    if (this.session.loggedIn) {
      this.router.navigate(["my"]);
    } else {
      this.router.navigate(["login"]);
    }
  }

  scrollbarVisible = false;
  refreshScrollbarVisible() {
    const body = document.getElementsByTagName("body")[0];
    this.scrollbarVisible = body.scrollHeight > body.clientHeight;
  }
}
