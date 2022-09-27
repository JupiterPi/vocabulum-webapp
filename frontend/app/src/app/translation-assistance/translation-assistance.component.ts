import { Component } from '@angular/core';
import {Router} from "@angular/router";

export type TAItem = {
  title: string,
  inflexible: boolean,
  forms: string[],
  definition: string,
  translations: string[],
  base_form: string
};

@Component({
  selector: 'app-translation-assistance',
  templateUrl: './translation-assistance.component.html',
  styleUrls: ['./translation-assistance.component.css']
})
export class TranslationAssistanceComponent {
  items: TAItem[] = [
    {
      title: "Asini",
      inflexible: false,
      forms: ["Gen. Sg.", "Nom. Pl."],
      definition: "asinus, asini m.",
      translations: ["der Esel", "das Maultier"],
      base_form: "asinus"
    },
    {
      title: "stant",
      inflexible: false,
      forms: ["3. Pers. Pl. Präs."],
      definition: "stare",
      translations: ["stehen", "dastehen"],
      base_form: "stare"
    },
    {
      title: "et",
      inflexible: true,
      forms: [],
      definition: "et",
      translations: ["und"],
      base_form: "et"
    },
    {
      title: "exspectant",
      inflexible: false,
      forms: ["3. Pers. Pl. Präs."],
      definition: "exspectare",
      translations: ["erwarten", "warten auf"],
      base_form: "exspectare"
    }
  ];

  constructor(private router: Router) {}

  showVocabulary(base_form: string) {
    this.router.navigate(["dictionary", base_form]);
  }
}
