import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CoreService} from "./core.service";
import {Observable} from "rxjs";
import {SessionService} from "../session.service";
import { environment } from 'src/environments/environment';

export type UserDetails = {
  username: string,
  email: string,
  isProUser: boolean,
  discordUsername: string,
  isAdmin: boolean
};

export type HistoryItemDTO = {
  time: number,
  mode: "cards" | "chat",
  direction: "lg" | "rand" | "gl",
  selection: string
}
export type HistoryItem = {
  time: Date,
  mode: "cards" | "chat",
  direction: "lg" | "rand" | "gl",
  selection: string
};

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  constructor(private http: HttpClient, private dataService: CoreService, private session: SessionService) {}

  /* user details */

  // GET /
  getUserDetails() {
    return new Observable<UserDetails>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.get<UserDetails>(environment.apiRoot + "/user", authHeaders).subscribe(subscriber);
      });
    });
  }

  // PUT /username
  changeUsername(newUsername: string) {
    return new Observable<UserDetails>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.put<UserDetails>(environment.apiRoot + "/user/username", {
          username: newUsername
        }, authHeaders).subscribe(subscriber);
      });
    });
  }

  // PUT /discordUsername
  changeDiscordUsername(newDiscordUsername: string) {
    return new Observable<UserDetails>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.put<UserDetails>(environment.apiRoot + "/user/discordUsername", {
          discordUsername: newDiscordUsername
        }, authHeaders).subscribe(subscriber);
      });
    });
  }

  /* history */

  getHistory() {
    return new Observable<HistoryItemDTO[]>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.get<HistoryItemDTO[]>(environment.apiRoot + "/user/history", authHeaders).subscribe(subscriber);
      });
    });
  }

  clearHistory() {
    return new Observable<HistoryItemDTO>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.delete<HistoryItemDTO>(environment.apiRoot + "/user/history", authHeaders).subscribe(subscriber);
      });
    });
  }
}
