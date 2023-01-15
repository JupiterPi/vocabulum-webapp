import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {DataService, Vocabulary} from "./data.service";

export type Direction = "lg" | "rand" | "gl";
type SessionOptions = {
  direction: Direction,
  selection: string
};

/* --- chat sessions --- */

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
export class ChatSessionService {
  constructor(private http: HttpClient, private dataService: DataService) {}

  authHeaders?: { headers: { Authorization: string } };

  // POST /create
  createSession(direction: Direction, selection: string) {
    const options: SessionOptions = {
      direction, selection
    };
    return this.http.post(this.dataService.backendRoot + "/api/session/chat/create", options, {
      responseType: "text",
      headers: this.authHeaders?.headers
    });
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

/* --- cards sessions --- */

export type CardsVocabulary = {
  base_form: string,
  direction: "lg" | "gl",
  german: string,
  latin: string
};

export type Feedback = {
  vocabulary: string,
  sentiment: Sentiment
};
export type Sentiment = "good" | "passable" | "bad";

export type FinishType = {
  repeat: boolean
};

export type Result = {
  score: number,
  done: boolean
};

@Injectable({
  providedIn: 'root'
})
export class CardsSessionService {
  constructor(private http: HttpClient, private dataService: DataService) {}

  authHeaders?: { headers: { Authorization: string } };

  // POST /create
  createSession(direction: Direction, selection: string) {
    const options: SessionOptions = {
      direction, selection
    };
    return this.http.post(this.dataService.backendRoot + "/api/session/cards/create", options, {
      responseType: "text",
      headers: this.authHeaders?.headers
    });
  }

  // GET /nextRound
  getNextRound(id: string) {
    return this.http.get<CardsVocabulary[]>(this.dataService.backendRoot + "/api/session/cards/" + id + "/nextRound");
  }

  // POST /feedback
  submitFeedback(id: string, feedback: Feedback[]) {
    return this.http.post<Result>(this.dataService.backendRoot + "/api/session/cards/" + id + "/feedback", feedback);
  }

  // POST /finish
  submitFinishType(id: string, repeat: boolean) {
    const finishType: FinishType = {
      repeat
    };
    return this.http.post(this.dataService.backendRoot + "/api/session/cards/" + id + "/finish", finishType);
  }
}

/* --- test trainer --- */

@Injectable({
  providedIn: 'root'
})
export class TestTrainerService {
  constructor(private http: HttpClient, private dataService: DataService) {}

  authHeaders?: { headers: { Authorization: string } };

  getVocabulariesAmount(selection: string) {
    return this.http.get(this.dataService.backendRoot + "/api/testtrainer/vocabulariesAmount/" + selection, {
      responseType: "text"
    });
  }
}
