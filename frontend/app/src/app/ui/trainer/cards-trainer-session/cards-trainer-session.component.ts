import {Component, OnInit} from '@angular/core';
import {
  CardsSessionService,
  CardsVocabulary,
  Direction,
  Feedback,
  Result,
  Sentiment
} from "../../../data/sessions.service";
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

  currentRound: CardsVocabulary[] = [];
  currentRoundFeedback: Feedback[] = [];

  currentVocabulary: CardsVocabulary = {
    base_form: "",
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
        this.setNextVocabulary();
      });
    });
  }

  setNextVocabulary() {
    if (this.currentRound.length === 0) {
      this.cardsSessions.getNextRound(this.sessionId).subscribe(round => {
        this.currentRound = round;
        this.currentRoundFeedback = [];

        this.currentVocabulary = round[0];
        this.lifecycle = "vocabulary";
      });
    } else {
      const currentIndex = this.currentRound.indexOf(this.currentVocabulary);
      if (currentIndex == this.currentRound.length - 1) {
        this.currentRound = [];
        this.cardsSessions.submitFeedback(this.sessionId, this.currentRoundFeedback).subscribe(result => {
          this.result = result;
          this.lifecycle = "result";
        });
      } else {
        this.currentVocabulary = this.currentRound[currentIndex + 1];
        this.lifecycle = "vocabulary";
      }
    }
  }

  showSolution() {
    this.lifecycle = "solution";
  }

  submitFeedback(sentiment: Sentiment) {
    this.currentRoundFeedback.push({
      vocabulary: this.currentVocabulary.base_form,
      sentiment
    });
    this.setNextVocabulary();
  }

  submitFinishType(repeat: boolean) {
    this.cardsSessions.submitFinishType(this.sessionId, repeat).subscribe(() => {
      if (repeat) {
        this.setNextVocabulary();
      } else {
        this.exit();
      }
    });
  }

  exit() {
    this.router.navigate(["trainer"]);
  }
}
