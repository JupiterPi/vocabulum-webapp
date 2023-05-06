import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Direction, TestTrainerService} from "../../../data/sessions.service";
import {CoreService} from "../../../data/core.service";

@Component({
  selector: 'app-test-trainer',
  templateUrl: './test-trainer.component.html',
  styleUrls: ['./test-trainer.component.scss']
})
export class TestTrainerComponent {
  constructor(private route: ActivatedRoute, private testTrainerService: TestTrainerService) {
    this.route.queryParams.subscribe(params => {
      this.direction = params["direction"] || "lg";
      this.selection = params["selection"] || "\"A\"";

      this.testTrainerService.getVocabulariesAmount(this.selection).subscribe(vocabulariesAmount => {
        this.vocabulariesAmount = parseInt(vocabulariesAmount);
        this.chosenAmount = Math.min(this.vocabulariesAmount, this.vocabulariesAmount > 20 ? 20 : 10);
      });
    });
  }

  direction: Direction = "lg";
  selection: string = "\"A\"";

  vocabulariesAmount = 0;
  chosenAmount = 0;

  changeChosenAmount(operation: number) {
    this.chosenAmount = Math.max( 1, Math.min(this.vocabulariesAmount, this.chosenAmount + operation));
  }

  openTest() {
    window.open(this.testTrainerService.getTestDocumentUrl(this.direction, this.selection, this.chosenAmount), "_blank");
  }
}
