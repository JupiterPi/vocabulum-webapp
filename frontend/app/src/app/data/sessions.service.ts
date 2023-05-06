import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CoreService} from "./core.service";
import {SessionService} from "../session.service";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

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
  constructor(private http: HttpClient, private dataService: CoreService, private session: SessionService) {}

  // POST /create
  createSession(direction: Direction, selection: string) {
    const options: SessionOptions = {
      direction, selection
    };
    return new Observable<string>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.post(environment.apiRoot + "/api/session/chat/create", options, {
          responseType: "text",
          headers: authHeaders.headers
        }).subscribe(subscriber);
      });
    });
  }

  // POST /:id/start
  startSession(id: string) {
    return this.http.post<BotMessage[]>(environment.apiRoot + "/api/session/chat/" + id + "/start", null);
  }

  // POST /:id/input
  sendUserInput(id: string, input: string) {
    const userInput: UserInput = {
      input,
      action: ""
    };
    return this.http.post<BotMessage[]>(environment.apiRoot + "/api/session/chat/" + id + "/input", userInput, {
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
    return this.http.post<BotMessage[]>(environment.apiRoot + "/api/session/chat/" + id + "/input", userInput, {
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
  constructor(private http: HttpClient, private dataService: CoreService, private session: SessionService) {}

  // POST /create
  createSession(direction: Direction, selection: string) {
    const options: SessionOptions = {
      direction, selection
    };
    return new Observable<string>(subscriber => {
      this.session.getAuthHeaders().subscribe(authHeaders => {
        this.http.post(environment.apiRoot + "/api/session/cards/create", options, {
          responseType: "text",
          headers: authHeaders.headers
        }).subscribe(subscriber);
      });
    });
  }

  // GET /nextRound
  getNextRound(id: string) {
    return this.http.get<CardsVocabulary[]>(environment.apiRoot + "/api/session/cards/" + id + "/nextRound");
  }

  // POST /feedback
  submitFeedback(id: string, feedback: Feedback[]) {
    return this.http.post<Result>(environment.apiRoot + "/api/session/cards/" + id + "/feedback", feedback);
  }

  // POST /finish
  submitFinishType(id: string, repeat: boolean) {
    const finishType: FinishType = {
      repeat
    };
    return this.http.post(environment.apiRoot + "/api/session/cards/" + id + "/finish", finishType);
  }
}

/* --- test trainer --- */

@Injectable({
  providedIn: 'root'
})
export class TestTrainerService {
  constructor(private http: HttpClient, private dataService: CoreService) {}

  getVocabulariesAmount(selection: string) {
    return this.http.get(environment.apiRoot + "/api/testtrainer/vocabulariesAmount/" + selection, {
      responseType: "text"
    });
  }

  getTestDocumentUrl(direction: Direction, selection: string, amount: number) {
    return `${environment.apiRoot}/api/testtrainer/test?direction=${direction}&selection=${selection}&amount=${amount}`;
  }
}
