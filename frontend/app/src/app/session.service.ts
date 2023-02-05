import {Injectable} from '@angular/core';
import {UserDetails} from "./data/users.service";
import {CookieService} from "ngx-cookie";
import {BehaviorSubject, filter, first, Observable} from "rxjs";
import {DataService} from "./data/data.service";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(
    private dataService: DataService,
    private http: HttpClient,
    private cookieService: CookieService
  ) {
    const auth = this.cookieService.get("auth");
    if (auth == null) {
      this.authHeaders$.next({headers: {"Authorization": "None"}});
      this.loggedIn$.next(false);
    } else {
      this.loginFromAuth(auth);
    }
  }

  user?: UserDetails;
  hasPro = false;

  private loggedIn$ = new BehaviorSubject<boolean | null>(null);
  getLoggedIn(): Observable<boolean> {
    return this.loggedIn$.pipe(filter(isNonNull), first());
  }

  private authHeaders$ = new BehaviorSubject<{headers: any} | null>(null);
  getAuthHeaders(): Observable<{headers: any}> {
    return this.authHeaders$.pipe(filter(isNonNull), first());
  }

  login(email: string, password: string) {
    this.loginFromAuth(btoa(email + ":" + password));
  }
  private loginFromAuth(auth: string) {
    this.loggedIn$.next(null);
    const authHeaders = {headers: {
      "Authorization": "Basic " + auth
    }};
    this.authHeaders$.next(authHeaders);
    this.http.post<UserDetails>(this.dataService.backendRoot + "/auth/login", null, authHeaders).subscribe(userDetails => {
      this.loggedIn$.next(true);
      this.user = userDetails;
      this.hasPro = userDetails.isProUser;

      this.cookieService.put("auth", auth, {
        secure: true,
        expires: new Date(new Date().getTime() + 1000*60*60*24)
      });
    }, _ => {
      this.loggedIn$.next(false);
    });
  }

  logout() {
    this.cookieService.remove("auth");
    this.user = undefined;
    this.hasPro = false;
    this.loggedIn$.next(false);
    this.authHeaders$.next(null);
  }
}

export function isNonNull<T>(value: T): value is NonNullable<T> {
  return value != null;
}
