import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Direction} from "../../../data/session.service";

export type Mode = "cards" | "chat";

@Component({
  selector: 'app-trainer-launcher',
  templateUrl: './trainer-launcher.component.html',
  styleUrls: ['./trainer-launcher.component.css']
})
export class TrainerLauncherComponent {
  selectedMode?: Mode;

  selectMode(mode: Mode) {
    this.selectedMode = mode;
  }

  selectedDirection?: Direction;

  selectDirection(direction: Direction) {
    this.selectedDirection = direction;
  }

  constructor(private router: Router, private route: ActivatedRoute) {}

  checkReady() {
    return this.selectedMode && this.selectedDirection;
  }

  submitStart() {
    if (this.checkReady()) {
      this.router.navigate([this.selectedMode], {
        relativeTo: this.route,
        queryParams: {
          direction: this.selectedDirection
        }
      });
    }
  }
}
