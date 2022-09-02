import {Component, OnInit} from '@angular/core';
import {Button, Message} from "../chat-view/chat-view.component";
import {BotMessage, SessionService} from "../data/session.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-test-chat-view',
  templateUrl: './test-chat-view.component.html',
  styleUrls: ['./test-chat-view.component.css']
})
export class TestChatViewComponent implements OnInit {
  messages: Message[] = [];
  buttons: Button[] = [];

  constructor(private sessions: SessionService, private router: Router) {}

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
        this.router.navigate(["dictionary"]);
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

  sessionId: string = "";

  ngOnInit(): void {
    this.sessions.createSession().subscribe(id => {
      this.sessionId = id;
      this.sessions.startSession(id).subscribe(messages => this.appendMessages(messages));
    });
  }
}
