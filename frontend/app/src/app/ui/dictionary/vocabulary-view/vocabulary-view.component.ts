import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {DataService, Vocabulary} from "../../../data/data.service";

@Component({
  selector: 'app-vocabulary-view',
  templateUrl: './vocabulary-view.component.html',
  styleUrls: ['./vocabulary-view.component.css']
})
export class VocabularyViewComponent implements OnInit {
  /*vocabulary: Vocabulary = {
    portion: "",
    translations: [],
    kind: "",
    base_form: "",
    definition: "",
    meta: []
  };*/
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
}
