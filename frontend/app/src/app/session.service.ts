import {Injectable} from '@angular/core';
import {UserDetails, UsersService} from "./data/users.service";
import {CookieService} from "ngx-cookie";
import {CardsSessionService, ChatSessionService, TestTrainerService} from "./data/sessions.service";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(
    private usersService: UsersService,
    private chatSessionsService: ChatSessionService,
    private cardsSessionService: CardsSessionService,
    private testTrainerService: TestTrainerService,
    private cookieService: CookieService
  ) {
    const auth = this.cookieService.get("auth");
    if (auth != null) this.loginFromAuth(auth);
  }

  loggedIn = false;
  authHeaders: {headers: {"Authorization": string}} = {headers: {"Authorization": "None"}};
  user?: UserDetails;
  hasPro = false;

  login(email: string, password: string) {
    this.loginFromAuth(btoa(email + ":" + password));
  }
  private loginFromAuth(auth: string) {
    this.loggedIn = true;
    this.authHeaders = {headers: {
      "Authorization": "Basic " + auth
    }};
    this.usersService.login(this.authHeaders).subscribe(userDetails => {
      this.usersService.authHeaders = this.authHeaders;
      this.chatSessionsService.authHeaders = this.authHeaders;
      this.cardsSessionService.authHeaders = this.authHeaders;
      this.testTrainerService.authHeaders = this.authHeaders;

      this.cookieService.put("auth", auth, {
        secure: true,
        expires: new Date(new Date().getTime() + 1000*60*60*24)
      });

      this.user = userDetails;
      this.hasPro = userDetails.isProUser;
    });
  }

  logout() {
    this.cookieService.remove("auth");
    this.loggedIn = false;
    this.user = undefined;
    this.hasPro = false;
  }
}
