import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {DataService, SearchResult} from "../data/data.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  query = "";
  results?: SearchResult[];

  constructor(private route: ActivatedRoute, private router: Router, private location: Location, private dataService: DataService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.query = params["q"];
      this.updateChange(this.query);
    });
  }

  updateChange(query: string) {
    if (query == "") {
      const url = this.router.createUrlTree([], {relativeTo: this.route});
      this.location.replaceState(url.toString());
    } else {
      const url = this.router.createUrlTree([], {relativeTo: this.route, queryParams: {q: query}});
      this.location.replaceState(url.toString());
    }
    // https://stackoverflow.com/a/46486677/13164753

    if (query != "") {
      this.dataService.getSearchResults(query).subscribe((results) => {
        this.results = results;
      });
    } else {
      this.results = [];
    }
  }

  openVocabularyPage(base_form: string) {
    this.router.navigate(["dictionary", base_form]);
  }
}
