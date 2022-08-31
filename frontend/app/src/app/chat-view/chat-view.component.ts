import {
  Component,
  DoCheck,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';

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
  chatInput: string = "";
  @Output() sendInput = new EventEmitter<string>();

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
    },
    {
      senderIsUser: false,
      messages: [
        "This is some really, really long chat message to test what the design does when one message has to span over multiple lines."
      ]
    },
    {
      senderIsUser: true,
      messages: [
        "This is some really, really long chat message to test what the design does when one message has to span over multiple lines."
      ]
    },
    {
      senderIsUser: false,
      messages: [
        "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?",
        "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?",
        "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?",
        "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?",
        "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?", "So, what do you think of the design?"
      ]
    }
  ];

  @ViewChild("messagesContainer") messagesContainer?: ElementRef;

  oldInputMessagesLength = 0;
  ngDoCheck() {
    if (this.inputMessages.length != this.oldInputMessagesLength) {
      this.clearMessages();
      for (let message of this.inputMessages) {
        this.appendMessage(message.message, message.senderIsUser, message.forceNewBlock);
      }
      this.oldInputMessagesLength = this.inputMessages.length;

      const id = setInterval(() => {
        const scrollElement = this.messagesContainer?.nativeElement;
        scrollElement.scrollTop = scrollElement.scrollHeight + scrollElement.clientHeight;
        clearInterval(id);
      }, 1);
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

  submitInput() {
    if (this.chatInput != "") {
      this.sendInput.emit(this.chatInput);
      this.chatInput = "";
    }
  }
}
