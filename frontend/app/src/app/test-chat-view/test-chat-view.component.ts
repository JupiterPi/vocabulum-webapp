import {Component, OnInit} from '@angular/core';
import {Message} from "../chat-view/chat-view.component";
import {BotMessage, SessionService} from "../data/session.service";

@Component({
  selector: 'app-test-chat-view',
  templateUrl: './test-chat-view.component.html',
  styleUrls: ['./test-chat-view.component.css']
})
export class TestChatViewComponent implements OnInit {
  messages: Message[] = [];

  constructor(private sessions: SessionService) {}

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

  appendMessages(messages: BotMessage[]) {
    for (let message of messages) {
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

  sessionId: string = "";

  ngOnInit(): void {
    this.sessions.createSession().subscribe(id => {
      this.sessionId = id;
      this.sessions.startSession(id).subscribe(messages => this.appendMessages(messages));
    });
  }
}
