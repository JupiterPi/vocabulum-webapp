import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {DataService, Vocabulary} from "../../../data/data.service";

@Component({
  selector: 'app-vocabulary-view',
  templateUrl: './vocabulary-view.component.html',
  styleUrls: ['./vocabulary-view.component.scss']
})
export class VocabularyViewComponent implements OnInit {
  vocabulary?: Vocabulary;

  constructor(private route: ActivatedRoute, private dataService: DataService) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const vocabulary = params["vocabulary"];

      this.dataService.getVocabulary(vocabulary).subscribe((vocabulary) => {
        this.vocabulary = vocabulary;
      });
    });
  }

  formatKind(kind: string) {
    switch (kind) {
      case "noun": return "Substantiv";
      case "adjective": return "Adjektiv";
      case "verb": return "Verb";
      case "inflexible": return "nicht flektiert";
    }
    return "";
  }
}
