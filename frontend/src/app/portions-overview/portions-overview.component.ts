import { Component } from '@angular/core';

@Component({
  selector: 'app-portions-overview',
  templateUrl: './portions-overview.component.html',
  styleUrls: ['./portions-overview.component.css']
})
export class PortionsOverviewComponent {
  portions = [
    {
      "name": "1",
      "vocabularyBlocks": [
        [
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "die Sonne"
              }
            ],
            "kind": "Noun",
            "base_form": "sol"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "die Ruhe"
              },
              {
                "important": true,
                "translation": "die Stille"
              },
              {
                "important": false,
                "translation": "das Schweigen"
              }
            ],
            "kind": "Noun",
            "base_form": "silentium"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "das (Land)Haus"
              },
              {
                "important": false,
                "translation": "die Villa"
              }
            ],
            "kind": "Noun",
            "base_form": "villa"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "der Hund"
              }
            ],
            "kind": "Noun",
            "base_form": "canis"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "heftig"
              },
              {
                "important": false,
                "translation": "hart"
              },
              {
                "important": false,
                "translation": "scharf"
              }
            ],
            "kind": "Adjective",
            "base_form": "acer"
          }
        ],
        [
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "kurz"
              }
            ],
            "kind": "Adjective",
            "base_form": "brevis"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "glücklich"
              }
            ],
            "kind": "Adjective",
            "base_form": "felix"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "sanft"
              },
              {
                "important": true,
                "translation": "zart"
              }
            ],
            "kind": "Adjective",
            "base_form": "clemens"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "schnell"
              }
            ],
            "kind": "Adjective",
            "base_form": "celer"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "hübsch"
              },
              {
                "important": false,
                "translation": "schön"
              }
            ],
            "kind": "Adjective",
            "base_form": "pulcher"
          },
          {
            "portion": "1",
            "translations": [
              {
                "important": true,
                "translation": "rufen"
              },
              {
                "important": true,
                "translation": "nennen"
              }
            ],
            "kind": "Verb",
            "base_form": "vocare"
          }
        ]
      ]
    },
    {
      "name": "A",
      "vocabularyBlocks": [
        [
          {
            "portion": "A",
            "translations": [
              {
                "important": true,
                "translation": "der Esel"
              }
            ],
            "kind": "Noun",
            "base_form": "asinus"
          },
          {
            "portion": "A",
            "translations": [
              {
                "important": true,
                "translation": "dastehen"
              },
              {
                "important": false,
                "translation": "aufrecht stehen"
              }
            ],
            "kind": "Verb",
            "base_form": "stare"
          },
          {
            "portion": "A",
            "translations": [
              {
                "important": true,
                "translation": "und"
              }
            ],
            "kind": "Inflexible",
            "base_form": "et"
          },
          {
            "portion": "A",
            "translations": [
              {
                "important": true,
                "translation": "erwarten"
              },
              {
                "important": false,
                "translation": "warten auf"
              }
            ],
            "kind": "Verb",
            "base_form": "exspectare"
          }
        ]
      ]
    }
  ];

  expandedPortions: number[] = [];
  isExpanded(portion: number) {
    return this.expandedPortions.includes(portion);
  }
  toggleExpand(portion: number) {
    if (this.expandedPortions.includes(portion)) {
      this.expandedPortions.splice(this.expandedPortions.indexOf(portion), 1);
    } else {
      this.expandedPortions.push(portion);
    }
  }
}
