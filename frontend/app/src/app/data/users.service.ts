import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DataService} from "./data.service";

export type CredentialsVerification = {
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
  constructor(private http: HttpClient, private dataService: DataService) {}

  devUserDetails: UserDetails = {
    username: "JupiterPi",
    email: "jupiterpi@vocabulum.de",
    isProUser: false
  };

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

  getUserDetails() {
    return this.devUserDetails;
  }

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
  verifyCredentials(username: string, password: string) {
    return this.http.post<CredentialsVerification>(this.dataService.backendRoot + "/auth/verifyCredentials", {
      username, password
    });
  }

  // POST /login
  login(username: string, password: string) {
    return this.http.post<UserDetails>(this.dataService.backendRoot + "/auth/login", null, {headers: {
      "Authorization": "Basic " + btoa(username + ":" + password)
    }});
  }
}
