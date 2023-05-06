import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

export type Portion = {
  name: string,
  vocabularyBlocks: VocabularyInPortion[][]
};
export type VocabularyInPortion = {
  portion: string,
  translations: {
    important: boolean,
    translation: string
  }[],
  kind: string,
  base_form: string
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
  }[],
  exampleSentences: {
    line: string,
    matchStart: number,
    matchEnd: number,
    lecture: string,
    lineIndex: number
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
  possibleWord?: {
    punctuation: boolean,
    inflexible: boolean,
    forms: string[],
    definition: string,
    translations: string[],
    base_form: string
  }
};

@Injectable({
  providedIn: 'root'
})
export class CoreService {
  constructor(private http: HttpClient) {}

  // GET /portion
  getPortions() {
    return this.http.get<Portion[]>(environment.apiRoot + "/api/portion");
  }

  // GET /vocabulary/:base_form
  getVocabulary(base_form: string) {
    return this.http.get<Vocabulary>(environment.apiRoot + "/api/vocabulary/" + base_form);
  }

  // GET /search/:query
  getSearchResults(query: string) {
    return this.http.get<SearchResult[]>(environment.apiRoot + "/api/search/" + query);
  }

  // GET /ta/search/:query
  getTAItems(query: string) {
    return this.http.get<TAItem[]>(environment.apiRoot + "/api/ta/search/" + encodeURIComponent(query));
  }
}
