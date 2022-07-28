import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  query = "no query";

  results = [
    {
      "found_parts": [
        {
          "str": "word",
          "in_query": true
        },
        {
          "str": "em",
          "in_query": false
        }
      ],
      "definition": "word, wordis m.",
      "base_form": "word",
      "form": "Akk. Sg.",
      "portion": "34"
    },
    {
      "found_parts": [
        {
          "str": "word",
          "in_query": true
        },
        {
          "str": "isterissimi",
          "in_query": false
        }
      ],
      "definition": "wordister, wordistera, wordisterum",
      "base_form": "wordister",
      "form": "Nom. Pl. m. Superl.",
      "portion": "34"
    }
  ];

  constructor(private route: ActivatedRoute, private router: Router, private location: Location) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.query = params["q"];
    });
  }

  updateChange(query: string) {
    const url = this.router.createUrlTree([], {relativeTo: this.route, queryParams: {q: query}});
    console.log(url.toString());
    this.location.replaceState(url.toString());
    // https://stackoverflow.com/a/46486677/13164753
  }

  log() {
    console.log(this.query);
  }

  openVocabularyPage(base_form: string) {
    this.router.navigate(["dictionary", base_form]);
  }
}
