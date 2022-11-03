import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Direction} from "../../../data/session.service";
import {Portion, Vocabulary} from "../../../data/data.service";
import {NestedTreeControl} from "@angular/cdk/tree";
import {MatTreeNestedDataSource} from "@angular/material/tree";

type Mode = "cards" | "chat";

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

  /*vocabularySelectorTreeControl = new NestedTreeControl<VocabularySelectorNode>(node => node.children);
  vocabularySelectorDataSource = new MatTreeNestedDataSource<VocabularySelectorNode>();
  hasChild = (_: number, node: VocabularySelectorNode) => !!node.children && node.children.length > 0;*/

  constructor(private router: Router, private route: ActivatedRoute) {
    /*this.vocabularySelectorDataSource.data = [
      {
        name: "1",
        children: [
          { name: "voc1" },
          { name: "voc2" },
          { name: "voc3" }
        ]
      },
      {
        name: "A",
        children: [
          { name: "voc1" },
          { name: "voc2" },
          { name: "voc3" }
        ]
      }
    ];*/
  }

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
