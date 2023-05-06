import {Injectable} from '@angular/core';
import {UserDetails} from "./data/users.service";
import {BehaviorSubject, filter, first, Observable} from "rxjs";
import {Auth, onAuthStateChanged, signInWithEmailAndPassword} from "@angular/fire/auth";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(private auth: Auth) {
    onAuthStateChanged(auth, user => {
      this.loggedIn$.next(user != null);
      if (user) {
        console.log(user);
        this.user = {
          username: user.displayName ?? user.email ?? "(username)",
          email: user.email ?? "(email)",
          isProUser: false,
          discordUsername: "logged in",
          isAdmin: false
        };
        this.hasPro = true;
        console.log(this.user);
      } else {
        console.log("logged out");
        this.user = undefined;
        this.hasPro = false;
      }
    });
  }

  user?: UserDetails;
  hasPro = false;

  loggedIn$ = new BehaviorSubject<boolean | null>(null);
  getLoggedIn(): Observable<boolean> {
    return this.loggedIn$.pipe(filter(isNonNull), first());
  }

  private authHeaders$ = new BehaviorSubject<{headers: any} | null>(null);
  getAuthHeaders(): Observable<{headers: any}> {
    return this.authHeaders$.pipe(filter(isNonNull), first());
  }

  login(email: string, password: string): Promise<any> {
    return signInWithEmailAndPassword(this.auth, email, password);
  }

  logout() {
    this.auth.signOut();
  }
}

export function isNonNull<T>(value: T): value is NonNullable<T> {
  return value != null;
}
