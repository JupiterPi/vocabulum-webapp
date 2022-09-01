import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {DataService} from "./data.service";

export type BotMessage = {
  messageParts: {
    message: string,
    bold: boolean,
    color: string
  }[],
  forceNewBlock: boolean;
};

export type UserInput = {
  input: string;
};

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor(private http: HttpClient, private dataService: DataService) {}

  // POST /create
  createSession() {
    return this.http.post(this.dataService.backendRoot + "/api/session/create", null, {responseType: "text"});
  }

  // POST /:id/start
  startSession(id: string) {
    return this.http.post<BotMessage[]>(this.dataService.backendRoot + "/api/session/" + id + "/start", null);
  }

  // POST /:id/input
  sendUserInput(id: string, input: string) {
    const userInput: UserInput = {
      input
    };
    return this.http.post<BotMessage[]>(this.dataService.backendRoot + "/api/session/" + id + "/input", userInput, {
      headers: new HttpHeaders({
        "Content-Type": "application/json"
      })
    });
  }
}
