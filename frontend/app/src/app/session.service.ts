import { Injectable } from '@angular/core';
import {UserDetails} from "./data/users.service";

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  loggedIn = false;
  user?: UserDetails;
  hasPro = false;

  login(user: UserDetails) {
    this.loggedIn = true;
    this.user = user;
    this.hasPro = user.isProUser;
  }
}
