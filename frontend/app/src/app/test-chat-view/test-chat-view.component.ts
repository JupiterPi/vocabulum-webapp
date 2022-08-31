import { Component} from '@angular/core';
import {Message} from "../chat-view/chat-view.component";

@Component({
  selector: 'app-test-chat-view',
  templateUrl: './test-chat-view.component.html',
  styleUrls: ['./test-chat-view.component.css']
})
export class TestChatViewComponent {
  messages: Message[] = [];

  message: string = "";
  senderIsUser: boolean = false;
  forceNewBlock: boolean = false;

  sendMessage() {
    this.messages.push({
      message: this.message,
      senderIsUser: this.senderIsUser,
      forceNewBlock: this.forceNewBlock
    });
    console.log("sending message", {
      message: this.message,
      senderIsUser: this.senderIsUser,
      forceNewBlock: this.forceNewBlock
    });
    console.log(this.messages);
  }
}
