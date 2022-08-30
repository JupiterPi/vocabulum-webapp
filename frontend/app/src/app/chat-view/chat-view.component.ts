import { Component } from '@angular/core';

export type MessageBlock = {
  senderIsUser: boolean,
  messages: string[]
};

@Component({
  selector: 'app-chat-view',
  templateUrl: './chat-view.component.html',
  styleUrls: ['./chat-view.component.css']
})
export class ChatViewComponent {
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
}
