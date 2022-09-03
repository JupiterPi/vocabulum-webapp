import {Component, OnInit} from '@angular/core';
import {Button, Message} from "../chat-view/chat-view.component";
import {BotMessage, Direction, SessionService} from "../../../data/session.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-chat-trainer-session',
  templateUrl: './chat-trainer-session.component.html',
  styleUrls: ['./chat-trainer-session.component.css']
})
export class ChatTrainerSessionComponent implements OnInit {
  messages: Message[] = [];
  buttons: Button[] = [];

  constructor(private sessions: SessionService, private route: ActivatedRoute, private router: Router) {}

  sessionId: string = "";

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const direction: Direction = params["direction"] || "lg";

      this.sessions.createSession(direction).subscribe(id => {
        this.sessionId = id;
        this.sessions.startSession(id).subscribe(messages => this.appendMessages(messages));
      });
    });
  }

  takeUserInput(message: string) {
    this.messages.push({
      message,
      senderIsUser: true,
      forceNewBlock: false
    });
    if (this.sessionId != "") {
      this.sessions.sendUserInput(this.sessionId, message).subscribe(messages => this.appendMessages(messages));
    }
  }

  takeButtonAction(action: string) {
    if (this.sessionId != "") {
      this.sessions.sendButtonAction(this.sessionId, action).subscribe(messages => this.appendMessages(messages));
    }
  }

  appendMessages(messages: BotMessage[]) {
    for (let message of messages) {
      if (message.exit) {
        this.router.navigate(["trainer"]);
        return;
      }

      if (message.hasButtons) {

        this.buttons = [];
        for (let button of message.buttons) {
          this.buttons.push({
            label: button.label,
            action: button.action
          });
        }

      } else {

        let messageStr = "";
        for (const part of message.messageParts) {
          const classNames = (part.bold ? "bold " : "") + part.color;
          messageStr += "<span style='color: red' class='" + classNames + "'>" + part.message + "</span>";
        }
        this.messages.push({
          message: messageStr,
          senderIsUser: false,
          forceNewBlock: message.forceNewBlock
        });

      }
    }
  }
}
