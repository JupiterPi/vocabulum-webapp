import {Injectable} from '@angular/core';
import {UserDetails, UsersService} from "./data/users.service";
import {CookieService} from "ngx-cookie";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(private usersService: UsersService, private cookieService: CookieService) {
    this.tryLoginFromCookie();
  }

  private tryLoginFromCookie() {
    const auth = this.cookieService.get("auth");
    if (auth != null) this.loginFromAuth(auth);
  }

  private writeLoginCookie(auth: string) {
    this.cookieService.put("auth", auth, {
      secure: true,
      expires: new Date(new Date().getTime() + 1000*60*60*24)
    });
  }

  loggedIn = false;
  user?: UserDetails;
  hasPro = false;

  login(email: string, password: string) {
    this.loginFromAuth(btoa(email + ":" + password));
  }
  private loginFromAuth(auth: string) {
    const authHeaders = {headers: {
      "Authorization": "Basic " + auth
    }};
    this.usersService.login(authHeaders).subscribe(userDetails => {
      this.loggedIn = true;
      this.usersService.authHeaders = authHeaders;
      this.writeLoginCookie(auth);

      this.user = userDetails;
      this.hasPro = userDetails.isProUser;
    });
  }
}
