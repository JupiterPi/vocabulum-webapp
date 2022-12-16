import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DataService, TAItem} from "../../data/data.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-translation-assistance',
  templateUrl: './translation-assistance.component.html',
  styleUrls: ['./translation-assistance.component.scss']
})
export class TranslationAssistanceComponent implements OnInit {
  query = "";
  ready = false;
  error = false;
  items: TAItem[] = [
    {
      title: "Asini",
      punctuation: false,
      inflexible: false,
      forms: ["Gen. Sg.", "Nom. Pl."],
      definition: "asinus, asini m.",
      translations: ["der Esel", "das Maultier"],
      base_form: "asinus"
    },
    {
      title: "stant",
      punctuation: false,
      inflexible: false,
      forms: ["3. Pers. Pl. Präs."],
      definition: "stare",
      translations: ["stehen", "dastehen"],
      base_form: "stare"
    },
    {
      title: ",",
      punctuation: true,
      inflexible: false,
      forms: [],
      definition: "",
      translations: [],
      base_form: ""
    },
    {
      title: "et",
      punctuation: false,
      inflexible: true,
      forms: [],
      definition: "et",
      translations: ["und"],
      base_form: "et"
    },
    {
      title: "exspectant",
      punctuation: false,
      inflexible: false,
      forms: ["3. Pers. Pl. Präs."],
      definition: "exspectare",
      translations: ["erwarten", "warten auf"],
      base_form: "exspectare"
    },
    {
      title: ".",
      punctuation: true,
      inflexible: false,
      forms: [],
      definition: "",
      translations: [],
      base_form: ""
    }
  ];

  hasPro = true;
  usesLeft = 3;

  constructor(private router: Router, private data: DataService, private route: ActivatedRoute, private location: Location) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.query = params["q"];
      this.updateChange(this.query);
    });
  }

  timerId?: number;
  updateChange(query: string) {
    if (query == "") {
      const url = this.router.createUrlTree([], {relativeTo: this.route});
      this.location.replaceState(url.toString());
    } else {
      const url = this.router.createUrlTree([], {relativeTo: this.route, queryParams: {q: query}});
      this.location.replaceState(url.toString());
    }

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
