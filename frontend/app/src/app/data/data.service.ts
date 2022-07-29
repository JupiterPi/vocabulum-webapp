import { Injectable, isDevMode } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Router} from "@angular/router";

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
  foundParts: {
    str: string,
    inQuery: boolean
  }[],
  definition: string,
  base_form: string,
  form: string,
  portion: string
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
      foundParts: [
        {
          str: "word",
          inQuery: true
        },
        {
          str: "em",
          inQuery: false
        }
      ],
      definition: "word, wordis m.",
      base_form: "word",
      form: "Akk. Sg.",
      portion: "34"
    },
    {
      foundParts: [
        {
          str: "word",
          inQuery: true
        },
        {
          str: "isterissimi",
          inQuery: false
        }
      ],
      definition: "wordister, wordistera, wordisterum",
      base_form: "wordister",
      form: "Nom. Pl. m. Superl.",
      portion: "34"
    }
  ];

  // GET /portion
  getPortions() {
    if (isDevMode()) {
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
    if (isDevMode()) {
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
    if (isDevMode()) {
      return new Observable<SearchResult[]>((observer) => {
        observer.next(this.devSearchResults);
        observer.complete();
      });
    } else {
      return this.http.get<SearchResult[]>(this.backendRoot + "/api/search/" + query);
    }
  }
}
