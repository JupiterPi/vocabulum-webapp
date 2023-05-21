import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SessionService} from "../../session.service";
import {environment} from "../../../environments/environment";

interface Text {
  text: string;
  level: "h1" | "h2" | "text";
}

interface Setting {
  title: string;
  description?: string;
  key: string;
  type: "toggle" | "options";
  options?: string[][];
}

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {
  constructor(private session: SessionService, private http: HttpClient) {
    this.session.getAuthHeaders().subscribe(authHeaders => {
      this.http.get<any>(environment.apiRoot + "/settings", authHeaders)
        .subscribe(settings => this.values = settings);
    });
  }

  values: any = {};

  settings: (Text | Setting)[] = [
    {
      text: "Abfragen",
      level: "h1"
    },
    {
      title: "Ungefähr richtige Antworten auch wiederholen",
      key: "session.repeat_passed",
      type: "toggle"
    },
    {
      text: "Chatabfragen",
      level: "h2"
    },
    {
      title: "Groß-/Kleinschreibung erzwingen",
      description: "macht bspw. \"das haus\" falsch",
      key: "chat.enforce_capitalization",
      type: "toggle"
    },
    {
      title: "Artikel erzwingen",
      description: "macht bspw. \"Haus\" falsch",
      key: "chat.enforce_article",
      type: "toggle"
    },
    {
      title: "Reihenfolge erzwingen",
      description: "dann musst du die Übersetzungen in der Reihenfolge angeben, in der sie im Buch stehen",
      key: "chat.enforce_order",
      type: "toggle"
    },
    {
      title: "Schwierigkeitsgrad bei Lat. → DE",
      description: "welche/wie viele der Übersetzungen du wissen musst",
      key: "chat.difficulty_lg",
      type: "options",
      options: [
        ["min. Hälfte der Übersetzungen", "HALF"],
        ["min. 1 wichtige Übersetzung", "PRIMARY"],
        ["alle wichtigen Übersetzungen", "IMPORTANT"],
        ["alle Übersetzungen", "ALL"]
      ]
    }
  ];

  isTextElement(e: Text | Setting) {
    return (e as Text).text != undefined;
  }

  updateValue(key: string, value: string | boolean) {
    this.session.getAuthHeaders().subscribe(authHeaders => {
      this.http.put<any>(environment.apiRoot + "/settings", { [key]: value }, authHeaders)
        .subscribe(settings => this.values = settings);
    });
  }

  text(e: Text | Setting) {
    return e as Text;
  }
  setting(e: Text | Setting) {
    return e as Setting;
  }

  protected readonly Object = Object;
}
