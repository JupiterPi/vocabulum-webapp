import { Component } from '@angular/core';
import {UserService, UserDetails, HistoryItem} from "../../../data/user.service";

type HistoryBlock = {
  startTime: Date,
  items: HistoryItem[]
};

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
  constructor(private user: UserService) {
    this.userDetails = user.getUserDetails();
    this.history = user.getHistory();
    this.sortHistoryItems(this.history);
  }

  userDetails: UserDetails = {
    username: "...",
    email: "..."
  };
  history: HistoryItem[] = [];

  obfuscateEmail(email: string) {
    const atIndex = email.lastIndexOf("@");
    if (atIndex == -1 || atIndex <= 2) return email;
    let str = email.substring(0, 2);
    for (let i = 0; i < atIndex - 2; i++) {
      str += "*";
    }
    return str + email.substring(atIndex);
  }

  showEmail() {
    alert("Deine E-Mail-Adresse lautet: " + this.userDetails.email);
  }

  changePassword() {
    prompt("Gib dein aktuelles Passwort ein:");
    prompt("Gib jetzt ein neues Passwort ein:");
    prompt("Wiederhole das neue Passwort:");
  }

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
          items: [item]
        };
      } else {
        block.items.push(item);
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

  formatDirection(direction: "lg" | "rand" | "gl") {
    if (direction == "lg") {
      return "Lat. → DE";
    } else if (direction == "rand") {
      return "zufälliger Richtung";
    } else if (direction == "gl") {
      return "DE → Lat.";
    }
    return null;
  }

  recallHistoryItem(item: HistoryItem) {
    alert("repeating");
  }
}
