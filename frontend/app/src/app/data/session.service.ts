import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {DataService} from "./data.service";

export type Direction = "lg" | "rand" | "gl";
type SessionOptions = {
  direction: Direction,
  selection: string
};

export type BotMessage = {
  messageParts: {
    message: string,
    bold: boolean,
    color: string
  }[],
  forceNewBlock: boolean,
  hasButtons: boolean,
  buttons: {
    label: string,
    action: string
  }[],
  exit: boolean
};

type UserInput = {
  input: string;
  action: string;
};

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(private http: HttpClient, private dataService: DataService) {}

  // POST /create
  createSession(direction: Direction, selection: string) {
    const options: SessionOptions = {
      direction, selection
    };
    return this.http.post(this.dataService.backendRoot + "/api/session/chat/create", options, {responseType: "text"});
  }

  // POST /:id/start
  startSession(id: string) {
    return this.http.post<BotMessage[]>(this.dataService.backendRoot + "/api/session/chat/" + id + "/start", null);
  }

  // POST /:id/input
  sendUserInput(id: string, input: string) {
    const userInput: UserInput = {
      input,
      action: ""
    };
    return this.http.post<BotMessage[]>(this.dataService.backendRoot + "/api/session/chat/" + id + "/input", userInput, {
      headers: new HttpHeaders({
        "Content-Type": "application/json"
      })
    });
  }

  // POST /:id/input
  sendButtonAction(id: string, action: string) {
    const userInput: UserInput = {
      input: "",
      action
    };
    return this.http.post<BotMessage[]>(this.dataService.backendRoot + "/api/session/chat/" + id + "/input", userInput, {
      headers: new HttpHeaders({
        "Content-Type": "application/json"
      })
    });
  }
}
