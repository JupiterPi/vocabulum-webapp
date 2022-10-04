import { Component } from '@angular/core';
import {HistoryItem, UserService} from "../../../data/user.service";

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
  constructor(private user: UserService) {
    this.history = user.getHistory();
    this.sortHistoryItems(this.history);
  }

  history: HistoryItem[] = [];

  sortedHistory: HistoryBlock[] = [];
  sortHistoryItems(history: HistoryItem[]) {
    this.sortedHistory = [];
    let previousTime: Date = history[0].time;
    let block: HistoryBlock = {
      startTime: history[0].time,
      items: []
    };
    for (let item of history) {
      if (item.time.getTime() - previousTime.getTime() > 30 * 60 * 1000) {
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

  formatTime(time: Date) {
    if (time.getTime() == new Date(0).getTime()) {
      return "Heute 15:36";
    } else {
      return "Heute 17:21";
    }
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
    alert("repeating " + item.time);
  }
}
