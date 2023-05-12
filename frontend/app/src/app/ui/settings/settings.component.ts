import {Component} from '@angular/core';

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
  values: Map<string, string | number | boolean> = new Map();

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
      key: "chat.enforce_capitalization",
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
        ["min. die Hälfte der Übersetzungen", "half"],
        ["min. 1 wichtige Übersetzung", "primary"],
        ["alle wichtigen Übersetzungen", "important"],
        ["alle Übersetzungen", "all"]
      ]
    }
  ];

  isTextElement(e: Text | Setting) {
    return (e as Text).text != undefined;
  }

  text(e: Text | Setting) {
    return e as Text;
  }
  setting(e: Text | Setting) {
    return e as Setting;
  }
}
