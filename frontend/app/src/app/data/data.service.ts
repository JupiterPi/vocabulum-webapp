import { Injectable, isDevMode } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {observable, Observable} from "rxjs";

export type Portion = {
  name: string,
  vocabularyBlocks: {
    portion: string,
    translations: {
      important: boolean,
      translation: string
    }[],
    kind: string,
    base_form: string
  }[][]
};

export type Vocabulary = {
  portion: string,
  translations: {
    important: boolean,
    translation: string
  }[],
  kind: string,
  base_form: string,
  definition: string,
  meta: {
    name: string,
    value: string
  }[]
};

export type SearchResult = {
  matchedForm: string;
  matchStart: number;
  matchEnd: number;
  inForm: string;
  additionalFormsCount: number;
  vocabulary: {
    portion: string,
    translations: {
      important: boolean,
      translation: string
    }[],
    kind: string,
    base_form: string,
    definition: string
  },
  inflexible: boolean
}

export type TAItem = {
  title: string,
  punctuation: boolean,
  inflexible: boolean,
  forms: string[],
  definition: string,
  translations: string[],
  base_form: string
};

@Injectable({
  providedIn: 'root'
})
export class DataService {
  backendRoot: string;

  constructor(private http: HttpClient) {
    const url = window.location.href;
    const isOnLocalhost = url.indexOf("localhost") > 0;
    this.backendRoot = isOnLocalhost ? "http://localhost:63109" : "http://api.vocabulum.de";
    console.log(this.backendRoot);
    /*this.http.get("/backendHost", {responseType: "text"}).subscribe((backendHost) => {
      console.log("backendRoot: " + backendHost);
      this.backendRoot = backendHost;
    });*/
  }

  devPortions: Portion[] = [
    {
      name: "1",
      vocabularyBlocks: [
        [
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "die Sonne"
              }
            ],
            kind: "Noun",
            base_form: "sol"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "die Ruhe"
              },
              {
                important: true,
                translation: "die Stille"
              },
              {
                important: false,
                translation: "das Schweigen"
              }
            ],
            kind: "Noun",
            base_form: "silentium"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "das (Land)Haus"
              },
              {
                important: false,
                translation: "die Villa"
              }
            ],
            kind: "Noun",
            base_form: "villa"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "der Hund"
              }
            ],
            kind: "Noun",
            base_form: "canis"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "heftig"
              },
              {
                important: false,
                translation: "hart"
              },
              {
                important: false,
                translation: "scharf"
              }
            ],
            kind: "Adjective",
            base_form: "acer"
          }
        ],
        [
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "kurz"
              }
            ],
            kind: "Adjective",
            base_form: "brevis"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "glücklich"
              }
            ],
            kind: "Adjective",
            base_form: "felix"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "sanft"
              },
              {
                important: true,
                translation: "zart"
              }
            ],
            kind: "Adjective",
            base_form: "clemens"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "schnell"
              }
            ],
            kind: "Adjective",
            base_form: "celer"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "hübsch"
              },
              {
                important: false,
                translation: "schön"
              }
            ],
            kind: "Adjective",
            base_form: "pulcher"
          },
          {
            portion: "1",
            translations: [
              {
                important: true,
                translation: "rufen"
              },
              {
                important: true,
                translation: "nennen"
              }
            ],
            kind: "Verb",
            base_form: "vocare"
          }
        ]
      ]
    },
    {
      name: "A",
      vocabularyBlocks: [
        [
          {
            portion: "A",
            translations: [
              {
                important: true,
                translation: "der Esel"
              }
            ],
            kind: "Noun",
            base_form: "asinus"
          },
          {
            portion: "A",
            translations: [
              {
                important: true,
                translation: "dastehen"
              },
              {
                important: false,
                translation: "aufrecht stehen"
              }
            ],
            kind: "Verb",
            base_form: "stare"
          },
          {
            portion: "A",
            translations: [
              {
                important: true,
                translation: "und"
              }
            ],
            kind: "Inflexible",
            base_form: "et"
          },
          {
            portion: "A",
            translations: [
              {
                important: true,
                translation: "erwarten"
              },
              {
                important: false,
                translation: "warten auf"
              }
            ],
            kind: "Verb",
            base_form: "exspectare"
          }
        ]
      ]
    }
  ];
  devVocabulary: Vocabulary = {
    portion: "1",
    translations: [
      {
        important: true,
        translation: "die Sonne"
      },
      {
        important: false,
        translation: "der Stern"
      }
    ],
    kind: "Noun",
    base_form: "sol",
    definition: "sol, solis m.",
    meta: [
      {
        name: "Deklinationsschema",
        value: "kons. Dekl."
      },
      {
        name: "Meta 1",
        value: "Something"
      },
      {
        name: "Meta 2",
        value: "Something else"
      },
      {
        name: "Meta 3",
        value: "Something different"
      },
      {
        name: "Meta 4",
        value: "Some other thing"
      }
    ]
  };
  devSearchResults: SearchResult[] = [
    {
      matchedForm: "wordem",
      matchStart: 0,
      matchEnd: 4,
      inForm: "Akk. Sg.",
      additionalFormsCount: 4,
      vocabulary: {
        portion: "34",
        translations: [
          {
            important: true,
            translation: "das Wort"
          },
          {
            important: false,
            translation: "der Satz"
          }
        ],
        kind: "noun",
        base_form: "word",
        definition: "word, wordis m."
      },
      inflexible: false
    },
    {
      matchedForm: "circumwordo",
      matchStart: 6,
      matchEnd: 10,
      inForm: "1. Pers. Sg.",
      additionalFormsCount: 1,
      vocabulary: {
        portion: "34",
        translations: [
          {
            important: true,
            translation: "wörtern"
          },
          {
            important: false,
            translation: "verlauten"
          }
        ],
        kind: "verb",
        base_form: "circumwordere",
        definition: "circumwordere, circumwordo, circumworsi, circumwortum"
      },
      inflexible: false
    },
    {
      matchedForm: "wordy",
      matchStart: 0,
      matchEnd: 4,
      inForm: "",
      additionalFormsCount: 0,
      vocabulary: {
        portion: "34",
        translations: [
          {
            important: true,
            translation: "wörtlich"
          },
          {
            important: false,
            translation: "worthaftig"
          }
        ],
        kind: "inflexible",
        base_form: "wordy",
        definition: "wordy"
      },
      inflexible: true
    }
  ];
  devTAItems: TAItem[] = [
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

  devMode = false;

  // GET /portion
  getPortions() {
    if (isDevMode() && this.devMode) {
      return new Observable<Portion[]>((observer) => {
        observer.next(this.devPortions);
        observer.complete();
      });
    } else {
      return this.http.get<Portion[]>(this.backendRoot + "/api/portion");
    }
  }

  // GET /vocabulary/:base_form
  getVocabulary(base_form: string) {
    if (isDevMode() && this.devMode) {
      return new Observable<Vocabulary>((observer) => {
        observer.next(this.devVocabulary);
        observer.complete();
      });
    } else {
      return this.http.get<Vocabulary>(this.backendRoot + "/api/vocabulary/" + base_form);
    }
  }

  // GET /search/:query
  getSearchResults(query: string) {
    if (isDevMode() && this.devMode) {
      return new Observable<SearchResult[]>((observer) => {
        observer.next(this.devSearchResults);
        observer.complete();
      });
    } else {
      return this.http.get<SearchResult[]>(this.backendRoot + "/api/search/" + query);
    }
  }

  // GET /ta/search/:query
  getTAItems(query: string) {
    if (isDevMode() && this.devMode) {
      return new Observable<TAItem[]>((observer ) => {
        observer.next(this.devTAItems);
        observer.complete();
      });
    } else {
      return this.http.get<TAItem[]>(this.backendRoot + "/api/ta/search/" + encodeURIComponent(query));
    }
  }
}
