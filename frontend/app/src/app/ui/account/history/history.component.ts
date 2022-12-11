import { Component } from '@angular/core';
import {HistoryItem, UsersService} from "../../../data/users.service";
import {min} from "rxjs";
import {Router} from "@angular/router";

type HistoryBlock = {
  startTime: Date,
  items: {
    showCondensed: boolean,
    item: HistoryItem
  }[]
};

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent {
  constructor(private user: UsersService, private router: Router) {
    user.getHistory().subscribe(history => {
      if (history.length > 0) {
        this.sortHistoryItems(history.map(dto => {
          return {
            time: new Date(dto.time),
            mode: dto.mode,
            direction: dto.direction,
            selection: dto.selection
          };
        }).reverse());
        this.empty = false;
      } else {
        this.sortedHistory = [];
        this.empty = true;
      }
    });
  }

  sortedHistory: HistoryBlock[] = [];
  empty = true;
  sortHistoryItems(history: HistoryItem[]) {
    this.sortedHistory = [];
    let previousTime: Date = history[0].time;
    let block: HistoryBlock = {
      startTime: history[0].time,
      items: []
    };
    for (let item of history) {
      const timeDifference = Math.abs(item.time.getTime() - previousTime.getTime());
      if (timeDifference > 30 * 60 * 1000) {
        this.sortedHistory.push(block);
        block = {
          startTime: item.time,
          items: [{ showCondensed: true, item }]
        };
      } else {
        block.items.push({ showCondensed: true, item });
      }
      previousTime = item.time;
    }
    this.sortedHistory.push(block);
  }

  formatDate(time: Date) {
    const diffTime = new Date().getTime() - time.getTime();
    const diffDays = diffTime / (1000 * 60 * 60 * 24);
    const diffHours = diffTime / (1000 * 60 * 60);
    const diffMinutes = diffTime / (1000 * 60);
    const dayAfterTime = new Date(time.getTime() + (1000*60*60*24));

    let timeOfDay = "";
    if (time.getHours() < 12) {
      timeOfDay = "Früh";
    } else if (time.getHours() < 15) {
      timeOfDay = "Mittag";
    } else if (time.getHours() < 19) {
      timeOfDay = "Nachmittag";
    } else {
      timeOfDay = "Abend";
    }

    if (
      time.getDate() === new Date().getDate()
      && time.getMonth() === new Date().getMonth()
      && time.getFullYear() === new Date().getFullYear()
    ) {
      if (diffHours > 5.0) {
        let minutesStr = time.getMinutes() + "";
        if (minutesStr.length <= 1) minutesStr = "0" + minutesStr;
        return `${time.getHours()}:${minutesStr}`;
      } else {
        if (diffHours > 4.0) {
          return "Vor 4 Stunden";
        } else if (diffHours > 3.0) {
          return "Vor 3 Stunden";
        } else if (diffHours > 2.0) {
          return "Vor 3 Stunden";
        } else if (diffHours > 1.0) {
          return "Vor 1 Stunde";
        } else if (diffMinutes > 45) {
          return "Vor 45 Minuten";
        } else if (diffMinutes > 30) {
          return "Vor 30 Minuten";
        } else if (diffMinutes > 20) {
          return "Vor 20 Minuten";
        } else if (diffMinutes > 15) {
          return "Vor 15 Minuten";
        } else if (diffMinutes > 10) {
          return "Vor 10 Minuten";
        } else if (diffMinutes > 5) {
          return "Vor 5 Minuten";
        } else {
          return "Gerade eben";
        }
      }
    } else if (
      dayAfterTime.getDate() === new Date().getDate()
      && dayAfterTime.getMonth() === new Date().getMonth()
      && dayAfterTime.getFullYear() === new Date().getFullYear()
    ) {
      return "Gestern " + timeOfDay;
    } else {
      if (diffDays > 5) {
        let yearStr = "";
        if (time.getFullYear() != new Date().getFullYear()) yearStr = time.getFullYear() + "";
        return `${time.getDate()}.${time.getMonth()+1}.${yearStr}`;
      } else {
        switch (time.getDay()) {
          case 0: return "Sonntag " + timeOfDay;
          case 1: return "Montag " + timeOfDay;
          case 2: return "Dienstag " + timeOfDay;
          case 3: return "Mittwoch " + timeOfDay;
          case 4: return "Donnerstag " + timeOfDay;
          case 5: return "Freitag " + timeOfDay;
          case 6: return "Samstag " + timeOfDay;
        }
      }
    }
    return "";
  }

  formatMode(mode: "cards" | "chat") {
    if (mode == "cards") {
      return "Karteikarten";
    } else if (mode == "chat") {
      return "Chat";
    }
    return null;
  }

  formatDirection(direction: "lg" | "rand" | "gl", specialSuffix: boolean) {
    if (direction == "lg") {
      return "Lat. → DE";
    } else if (direction == "rand") {
      return specialSuffix ? "zufälliger Richtung" : "zufällige Richtung";
    } else if (direction == "gl") {
      return "DE → Lat.";
    }
    return null;
  }

  expandHistoryItem(item: {showCondensed: boolean, item: HistoryItem}) {
    this.sortedHistory.forEach(block => {
      block.items.forEach(item => item.showCondensed = true);
    });
    /*for (let blockItem of block.items) {
      if (blockItem.item == item) {
        blockItem.showCondensed = false;
        return;
      }
    }*/
    item.showCondensed = false;
  }

  repeatHistoryItem(item: HistoryItem) {
    this.router.navigate(["trainer", item.mode], {
      queryParams: {
        direction: item.direction,
        selection: item.selection
      }
    });
  }
}
