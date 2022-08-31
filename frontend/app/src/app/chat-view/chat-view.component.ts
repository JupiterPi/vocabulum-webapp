import {Component, DoCheck, Input, OnChanges, SimpleChanges} from '@angular/core';

export type MessageBlock = {
  senderIsUser: boolean,
  messages: string[]
};

export type Message = {
  message: string,
  senderIsUser: boolean,
  forceNewBlock: boolean
};

@Component({
  selector: 'app-chat-view',
  templateUrl: './chat-view.component.html',
  styleUrls: ['./chat-view.component.css']
})
export class ChatViewComponent implements DoCheck {
  @Input() inputMessages: Message[] = [];
  @Input() test?: string;

  messages: MessageBlock[] = [
    {
      senderIsUser: false,
      messages: [
        "Hello World!",
        "Hello?",
        "Anyone there?",
        "It seems I am all alone."
      ]
    },
    {
      senderIsUser: true,
      messages: [
        "No, please!",
        "You are not alone!"
      ]
    },
    {
      senderIsUser: false,
      messages: [
        "Who is there?"
      ]
    },
    {
      senderIsUser: true,
      messages: [
        "I am here, the user!"
      ]
    },
    {
      senderIsUser: false,
      messages: [
        "Nice to meet you, user.",
        "What do you think of this design?"
      ]
    }
  ];

  oldInputMessagesLength = 0;
  ngDoCheck() {
    if (this.inputMessages.length != this.oldInputMessagesLength) {
      this.clearMessages();
      for (let message of this.inputMessages) {
        this.appendMessage(message.message, message.senderIsUser, message.forceNewBlock);
      }
      this.oldInputMessagesLength = this.inputMessages.length;
    }
  }

  clearMessages() {
    this.messages = [];
  }

  appendMessage(message: string, senderIsUser: boolean, forceNewBlock: boolean) {
    if (this.messages.length > 0 && !forceNewBlock) {
      const lastBlock = this.messages[this.messages.length-1];
      if (lastBlock.senderIsUser == senderIsUser) {
        lastBlock.messages.push(message);
        return;
      }
    }
    this.messages.push({
      senderIsUser: senderIsUser,
      messages: [message]
    });
  }
}
