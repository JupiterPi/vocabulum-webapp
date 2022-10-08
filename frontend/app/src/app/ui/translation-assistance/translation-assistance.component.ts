import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {DataService, TAItem} from "../../data/data.service";

@Component({
  selector: 'app-translation-assistance',
  templateUrl: './translation-assistance.component.html',
  styleUrls: ['./translation-assistance.component.scss']
})
export class TranslationAssistanceComponent {
  query: string = "";
  ready: boolean = false;
  error: boolean = false;
  items: TAItem[] = [];

  constructor(private router: Router, private data: DataService) {}

  timerId?: number;
  updateChange(query: string) {
    this.ready = false;
    this.error = false;
    clearTimeout(this.timerId);

    if (query === "") return;
    this.timerId = setTimeout(() => {
      this.data.getTAItems(query).subscribe({
        next: (items) => {
          this.items = items;
          this.ready = true;
        },
        error: (err) => {
          this.error = true;
        }
      });
    }, 250);
  }

  showVocabulary(base_form: string) {
    this.router.navigate(["dictionary", base_form]);
  }
}
