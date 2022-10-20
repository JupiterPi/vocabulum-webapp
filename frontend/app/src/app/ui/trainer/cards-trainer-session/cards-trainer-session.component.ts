import { Component } from '@angular/core';

type Result = "good" | "passable" | "bad";

@Component({
  selector: 'app-cards-trainer-session',
  templateUrl: './cards-trainer-session.component.html',
  styleUrls: ['./cards-trainer-session.component.scss']
})
export class CardsTrainerSessionComponent {
  solutionShown = false;

  showSolution() {
    this.solutionShown = true;
  }

  submitSentiment(result: Result) {
    this.solutionShown = false;
  }
}
