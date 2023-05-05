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
      possibleWord: {
        punctuation: false,
        inflexible: false,
        forms: ["Gen. Sg.", "Nom. Pl."],
        definition: "asinus, asini m.",
        translations: ["*der Esel*", "das Maultier"],
        base_form: "asinus"
      }
    },
    {
      title: "stant",
      possibleWord: {
        punctuation: false,
        inflexible: false,
        forms: ["3. Pers. Pl. Präs."],
        definition: "stare, sto, stavi, statum",
        translations: ["*stehen*", "dastehen"],
        base_form: "stare"
      }
    },
    {
      title: ",",
      possibleWord: {
        punctuation: true,
        inflexible: false,
        forms: [],
        definition: "",
        translations: [],
        base_form: ""
      }
    },
    {
      title: "et",
      possibleWord: {
        punctuation: false,
        inflexible: true,
        forms: [],
        definition: "et",
        translations: ["*und*"],
        base_form: "et"
      }
    },
    {
      title: "exspectant",
      possibleWord: {
        punctuation: false,
        inflexible: false,
        forms: ["3. Pers. Pl. Präs."],
        definition: "exspectare, exspecto, exspectavi, exspectatum",
        translations: ["*erwarten*", "warten auf"],
        base_form: "exspectare"
      }
    },
    {
      title: ".",
      possibleWord: {
        punctuation: true,
        inflexible: false,
        forms: [],
        definition: "",
        translations: [],
        base_form: ""
      }
    }
  ];

  hasPro = true;
  usesLeft = 3;

  constructor(private router: Router, private data: DataService, private route: ActivatedRoute, private location: Location) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const query = params["q"];
      if (!(query == null || query == "")) {
        this.query = query;
        this.updateChange(this.query);
      }
    });
  }

  timer?: NodeJS.Timeout;
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
    clearTimeout(this.timer);

    if (query === "") return;
    this.timer = setTimeout(() => {
      this.data.getTAItems(query).subscribe({
        next: (items) => {
          this.items = items;
          items.forEach(item => {
            if (!item.possibleWord) this.error = true;
          });
          this.ready = true;
        },
        error: () => {
          this.error = true;
        }
      });
    }, 250);
  }

  isTranslationImportant(translation: string) {
    return translation.startsWith("*");
  }
  formatTranslation(translation: string) {
    if (translation.startsWith("*")) translation = translation.substring(1, translation.length-1);
    return translation;
  }

  showVocabulary(base_form: string) {
    this.router.navigate(["dictionary", base_form]);
  }
}
