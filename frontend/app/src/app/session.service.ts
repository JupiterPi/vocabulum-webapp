import {Injectable} from '@angular/core';
import {UserDetails, UsersService} from "./data/users.service";
import {CookieService} from "ngx-cookie";
import {CardsSessionService, ChatSessionService} from "./data/sessions.service";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(
    private usersService: UsersService,
    private chatSessionsService: ChatSessionService,
    private cardsSessionService: CardsSessionService,
    private cookieService: CookieService
  ) {
    const auth = this.cookieService.get("auth");
    if (auth != null) this.loginFromAuth(auth);
  }

  loggedIn = false;
  user?: UserDetails;
  hasPro = false;

  login(email: string, password: string) {
    this.loginFromAuth(btoa(email + ":" + password));
  }
  private loginFromAuth(auth: string) {
    this.loggedIn = true;
    const authHeaders = {headers: {
      "Authorization": "Basic " + auth
    }};
    this.usersService.login(authHeaders).subscribe(userDetails => {
      this.usersService.authHeaders = authHeaders;
      this.chatSessionsService.authHeaders = authHeaders;
      this.cardsSessionService.authHeaders = authHeaders;
      console.log("set: " + JSON.stringify(this.cardsSessionService.authHeaders));

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
