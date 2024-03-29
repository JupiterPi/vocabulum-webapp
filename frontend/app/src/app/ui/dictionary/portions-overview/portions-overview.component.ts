import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CoreService, Portion} from "../../../data/core.service";

@Component({
  selector: 'app-portions-overview',
  templateUrl: './portions-overview.component.html',
  styleUrls: ['./portions-overview.component.scss']
})
export class PortionsOverviewComponent implements OnInit {
  portions?: Portion[];

  constructor(private router: Router, private dataService: CoreService) {}

  ngOnInit() {
    this.dataService.getPortions().subscribe((portions) => {
      this.portions = portions;

      for (let i = 0; i < this.portions.length; i++) {
        this.expandedPortions.push(i);
      }
    });
  }

  expandedPortions: number[] = [];
  isExpanded(portion: number) {
    return this.expandedPortions.includes(portion);
  }
  toggleExpand(portion: number) {
    if (this.expandedPortions.includes(portion)) {
      this.expandedPortions.splice(this.expandedPortions.indexOf(portion), 1);
    } else {
      this.expandedPortions.push(portion);
    }
  }

  openVocabularyPage(vocabulary: {base_form: string}) {
    this.router.navigate(["dictionary", vocabulary.base_form]);
  }
}
