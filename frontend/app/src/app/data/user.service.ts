import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

export type UserDetails = {
  username: string,
  email: string
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
export class UserService {
  constructor(private http: HttpClient) {}

  devUserDetails: UserDetails = {
    username: "JupiterPi",
    email: "jupiterpi@vocabulum.de"
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
}
