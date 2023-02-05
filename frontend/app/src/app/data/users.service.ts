import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DataService} from "./data.service";
import {Observable} from "rxjs";
import {SessionService} from "../session.service";

export type CredentialsVerification = {
  email: string,
  valid: boolean
}

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
  constructor(private http: HttpClient, private dataService: DataService, private session: SessionService) {}

  /*devHistory: HistoryItem[] = [
    {
      time: new Date(0),
      mode: "cards",
      direction: "lg",
      selection: "37"
    },
    {
      time: new Date(0),
      mode: "cards",
      direction: "gl",
      selection: "38"
    },
    {
      time: new Date(),
      mode: "chat",
      direction: "rand",
      selection: "39"
    },
    {
      time: new Date(),
      mode: "chat",
      direction: "gl",
      selection: "40"
    }
  ];*/

  // POST /register
  registerNewUser(username: string, email: string, password: string) {
    return this.http.post(this.dataService.backendRoot + "/auth/register", {
      username, email, password
    });
  }

  // POST /confirmRegistration
  confirmRegistration(id: string) {
    return this.http.post(this.dataService.backendRoot + "/auth/confirmRegistration/" + id, null, {responseType: "text"});
  }

  // POST /verifyCredentials
  verifyCredentials(email: string, password: string) {
    return this.http.post<CredentialsVerification>(this.dataService.backendRoot + "/auth/verifyCredentials", {
      username: email, password
    });
  }

  // POST /login
  // in session.service.ts

  /* user details */

  // PUT /username
  changeUsername(newUsername: string) {
    return new Observable<UserDetails>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.put<UserDetails>(this.dataService.backendRoot + "/user/username", {
          username: newUsername
        }, authHeaders).subscribe(subscriber);
      });
    });
  }

  // PUT /password
  changePassword(newPassword: string) {
    return new Observable(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.put(this.dataService.backendRoot + "/user/password", {
          password: newPassword
        }, authHeaders).subscribe(subscriber);
      });
    });
  }

  // PUT /discordUsername
  changeDiscordUsername(newDiscordUsername: string) {
    return new Observable<UserDetails>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.put<UserDetails>(this.dataService.backendRoot + "/user/discordUsername", {
          discordUsername: newDiscordUsername
        }, authHeaders).subscribe(subscriber);
      });
    });
  }

  /* history */

  getHistory() {
    return new Observable<HistoryItemDTO[]>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.get<HistoryItemDTO[]>(this.dataService.backendRoot + "/user/history", authHeaders).subscribe(subscriber);
      });
    });
  }

  clearHistory() {
    return new Observable<HistoryItemDTO>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.delete<HistoryItemDTO>(this.dataService.backendRoot + "/user/history", authHeaders).subscribe(subscriber);
      });
    });
  }
}
