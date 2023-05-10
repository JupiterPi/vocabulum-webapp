import {Injectable} from '@angular/core';
import {UserDetails, UsersService} from "./data/users.service";
import {BehaviorSubject, filter, first, Observable} from "rxjs";
import {Auth, createUserWithEmailAndPassword, onAuthStateChanged, signInWithEmailAndPassword, User} from "@angular/fire/auth";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(private auth: Auth, private http: HttpClient) {
    onAuthStateChanged(auth, user => {
      if (user) {
        this.user = user;
        this.userDetails = {
          username: user.displayName ?? user.email?.split("@")[0] ?? "...",
          isProUser: false,
          discordUsername: undefined
        };

        user.getIdToken().then(token => {
          const authHeaders = {
            headers: {"Authorization": `Bearer ${token}`}
          };
          this.authHeaders$.next(authHeaders);
          this.loadUserDetails(authHeaders);
        });

        this.loggedIn$.next(true);
      } else {
        this.user = undefined;
        this.userDetails = undefined;
        this.hasPro = false;
        this.loggedIn$.next(false);
      }
    });
  }

  user?: User;
  userDetails?: UserDetails;
  hasPro = false;
  loadUserDetails(authHeaders: {headers: any}) {
    this.http.get<UserDetails>(environment.apiRoot + "/user", authHeaders).subscribe(userDetails => this.setUserDetails(userDetails));
  }
  setUserDetails(userDetails: UserDetails) {
    this.userDetails = userDetails;
    this.hasPro = userDetails.isProUser;
  }

  loggedIn$ = new BehaviorSubject<boolean | null>(null);
  getLoggedIn(): Observable<boolean> {
    return this.loggedIn$.pipe(filter(isNonNull), first());
  }

  private authHeaders$ = new BehaviorSubject<{headers: any} | null>(null);
  getAuthHeaders(): Observable<{headers: any}> {
    return this.authHeaders$.pipe(filter(isNonNull), first());
  }

  isAdmin(): Observable<boolean> {
    return new Observable<boolean>(subscriber => {
      this.getLoggedIn().subscribe(loggedIn => {
        if (loggedIn) {
          this.user!!.getIdTokenResult().then(token => {
            subscriber.next(token.claims["admin"] != null);
          });
        } else {
          subscriber.next(false);
        }
      })
    });
  }

  login(email: string, password: string): Promise<any> {
    return signInWithEmailAndPassword(this.auth, email, password);
  }

  createAccountAndLogin(username: string, email: string, password: string) {
    return new Observable<UserDetails>(subscriber => {
      createUserWithEmailAndPassword(this.auth, email, password).then(credentials => {
        credentials.user.getIdToken().then(token => {
          const authHeaders = {
            headers: {"Authorization": `Bearer ${token}`}
          };
          this.http.post<UserDetails>(environment.apiRoot + "/auth/register", { username }, authHeaders).subscribe(subscriber);
          setTimeout(() => {
            this.loadUserDetails(authHeaders)
          }, 1000);
        });
      });
    });
  }

  logout() {
    this.auth.signOut();
  }
}

export function isNonNull<T>(value: T): value is NonNullable<T> {
  return value != null;
}
