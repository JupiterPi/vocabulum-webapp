import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-vocabulary-view',
  templateUrl: './vocabulary-view.component.html',
  styleUrls: ['./vocabulary-view.component.css']
})
export class VocabularyViewComponent implements OnInit {
  str: string = "not set";

  vocabulary = {
    "portion": "1",
    "translations": [
      {
        "important": true,
        "translation": "die Sonne"
      },
      {
        "important": false,
        "translation": "der Stern"
      }
    ],
    "kind": "Noun",
    "base_form": "sol",
    "base_forms": "sol, solis m.",
    "meta": [
      {
        "name": "Deklinationsschema",
        "value": "kons. Dekl."
      },
      {
        "name": "Meta 1",
        "value": "Something"
      },
      {
        "name": "Meta 2",
        "value": "Something else"
      },
      {
        "name": "Meta 3",
        "value": "Something different"
      },
      {
        "name": "Meta 4",
        "value": "Some other thing"
      }
    ]
  };

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.str = params["vocabulary"];
    });
  }
}
