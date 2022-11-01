import {Component, OnInit} from '@angular/core';
import {CardsSessionService, CardsVocabulary, Direction, Result} from "../../../data/session.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-cards-trainer-session',
  templateUrl: './cards-trainer-session.component.html',
  styleUrls: ['./cards-trainer-session.component.scss']
})
export class CardsTrainerSessionComponent implements OnInit {
  lifecycle: "vocabulary" | "solution" | "result" = "vocabulary";

  constructor(private cardsSessions: CardsSessionService, private route: ActivatedRoute, private router: Router) {}

  sessionId: string = "";

  vocabulary: CardsVocabulary = {
    direction: "lg",
    latin: "",
    german: ""
  };

  result: Result = {
    score: 0.0,
    done: false
  };

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const direction: Direction = params["direction"] || "lg";
      const selection: string = params["selection"] || "\"A\"";

      this.cardsSessions.createSession(direction, selection).subscribe(id => {
        this.sessionId = id;
        this.retrieveNextVocabulary();
      });
    });
  }

  retrieveNextVocabulary() {
    this.cardsSessions.getNextVocabulary(this.sessionId).subscribe(vocabulary => {
      console.log(vocabulary);
      this.vocabulary = vocabulary;
    });
    this.lifecycle = "vocabulary";
  }

  showSolution() {
    this.lifecycle = "solution";
  }

  submitSentiment(result: "good" | "passable" | "bad") {
    this.cardsSessions.submitSentiment(this.sessionId, result).subscribe(nextType => {
      if (nextType.nextType == "next_vocabulary") {
        this.retrieveNextVocabulary();
      } else {
        this.cardsSessions.getResult(this.sessionId).subscribe(result => {
          this.result = result;
        });
        this.lifecycle = "result";
      }
    });
  }

  submitFinishType(repeat: boolean) {
    this.cardsSessions.submitFinishType(this.sessionId, repeat).subscribe(() => {
      if (repeat) {
        this.retrieveNextVocabulary();
      } else {
        this.exit();
      }
    });
  }

  exit() {
    this.router.navigate(["trainer"]);
  }
}
