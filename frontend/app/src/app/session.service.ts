import {Injectable} from '@angular/core';
import {UserDetails} from "./data/users.service";
import {BehaviorSubject, filter, first, Observable} from "rxjs";
import {
  Auth,
  createUserWithEmailAndPassword,
  getAdditionalUserInfo,
  GoogleAuthProvider,
  onAuthStateChanged,
  signInWithEmailAndPassword,
  signInWithPopup,
  User,
  UserCredential
} from "@angular/fire/auth";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(private auth: Auth, private http: HttpClient) {
    auth.languageCode = "de";

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
          this.loadUserDetails(authHeaders).subscribe();
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
    return new Observable<void>(subscriber => {
      this.http.get<UserDetails>(environment.apiRoot + "/user", authHeaders).subscribe(userDetails => {
        this.setUserDetails(userDetails);
        subscriber.next();
      });
    });
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

  getAvatarUrl() {
    const effectiveUsername = this.userDetails?.username ?? this.user?.displayName ?? this.user?.email ?? "";
    return this.user?.photoURL ?? "https://api.dicebear.com/6.x/initials/svg?backgroundType=gradientLinear&fontFamily=Times%20New%20Roman&chars=1&fontSize=70&backgroundColor=F14321,16708D&seed=" + effectiveUsername;
  }

  login(email: string, password: string): Promise<any> {
    return signInWithEmailAndPassword(this.auth, email, password);
  }

  private googleProvider = new GoogleAuthProvider();
  loginGoogle() {
    return new Observable<void>(subscriber => {
      signInWithPopup(this.auth, this.googleProvider).then(credentials => {
        if (getAdditionalUserInfo(credentials)?.isNewUser) {
          this.registerAccount(credentials, credentials.user.displayName ?? credentials.user.email ?? "Neuer Nutzer")
            .subscribe(subscriber);
        } else {
          subscriber.next();
        }
      });
    });
  }

  createAccountAndLogin(username: string, email: string, password: string) {
    return new Observable<void>(subscriber => {
      createUserWithEmailAndPassword(this.auth, email, password).then(credentials => {
        this.registerAccount(credentials, username).subscribe(subscriber);
      });
    });
  }

  private registerAccount(credentials: UserCredential, username: string) {
    return new Observable<void>(subscriber => {
      credentials.user.getIdToken().then(token => {
        const authHeaders = {
          headers: {"Authorization": `Bearer ${token}`}
        };
        let userDetails: UserDetails | undefined;
        this.http.post<UserDetails>(environment.apiRoot + "/auth/register", { username }, authHeaders).subscribe(details => {
          userDetails = details;
        });
        setTimeout(() => {
          this.loadUserDetails(authHeaders).subscribe(subscriber);
        }, 1000);
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
