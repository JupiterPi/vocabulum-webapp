import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DataService} from "./data.service";
import {SessionService} from "../session.service";

export type CredentialsVerification = {
  email: string,
  valid: boolean
}

export type UserDetails = {
  username: string,
  email: string,
  isProUser: boolean
};

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
  constructor(private http: HttpClient, private dataService: DataService, private sessionService: SessionService) {}

  devHistory: HistoryItem[] = [
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
  ];

  getHistory() {
    return this.devHistory;
  }

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

  authHeaders?: { headers: { Authorization: string } };
  makeAuthHeaders(email: string, password: string) {
    return {headers: {
      "Authorization": "Basic " + btoa(email + ":" + password)
    }};
  }

  // POST /login
  login(email: string, password: string) {
    return this.http.post<UserDetails>(this.dataService.backendRoot + "/auth/login", null, this.makeAuthHeaders(email, password));
  }

  // PUT /username
  changeUsername(newUsername: string) {
    return this.http.put<UserDetails>(this.dataService.backendRoot + "/auth/username", {
      username: newUsername
    }, this.authHeaders);
  }

  // PUT /password
  changePassword(newPassword: string) {
    return this.http.put(this.dataService.backendRoot + "/auth/password", {
      password: newPassword
    }, this.authHeaders);
  }
}
