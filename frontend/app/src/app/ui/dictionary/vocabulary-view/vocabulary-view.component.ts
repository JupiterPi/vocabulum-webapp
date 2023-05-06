import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CoreService, Vocabulary} from "../../../data/core.service";

@Component({
  selector: 'app-vocabulary-view',
  templateUrl: './vocabulary-view.component.html',
  styleUrls: ['./vocabulary-view.component.scss']
})
export class VocabularyViewComponent implements OnInit {
  vocabulary?: Vocabulary;

  constructor(private route: ActivatedRoute, private dataService: CoreService) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const vocabulary = params["vocabulary"];

      this.dataService.getVocabulary(vocabulary).subscribe((vocabulary) => {
        this.vocabulary = vocabulary;
        this.exampleSentencesShown = vocabulary.exampleSentences.slice(0, 5);
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

  exampleSentencesShown: {line: string, matchStart: number, matchEnd: number, lecture: string, lineIndex: number}[] = [];
  expandExampleSentences() {
    this.exampleSentencesShown = this.vocabulary?.exampleSentences ?? [];
  }
}
