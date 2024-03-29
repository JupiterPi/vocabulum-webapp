import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Direction} from "../../../data/sessions.service";
import {SessionService} from "../../../session.service";

type Mode = "cards" | "chat" | "test";

interface VocabularySelectorNode {
  name: string;
  children?: VocabularySelectorNode[];
}

@Component({
  selector: 'app-trainer-launcher',
  templateUrl: './trainer-launcher.component.html',
  styleUrls: ['./trainer-launcher.component.scss']
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

  selectionString = "";

  constructor(private router: Router, private route: ActivatedRoute, public session: SessionService) {}

  checkReady() {
    return this.selectedMode && this.selectedDirection && this.selectionString != "";
  }

  submitStart() {
    if (this.checkReady()) {
      this.router.navigate([this.selectedMode], {
        relativeTo: this.route,
        queryParams: {
          direction: this.selectedDirection,
          selection: this.selectionString
        }
      });
    }
  }

  userDetails() {
    return JSON.stringify(this.session.userDetails);
  }
}
