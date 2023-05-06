import {Component, Input} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CoreService} from "../../../data/core.service";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'ai-block',
  templateUrl: './ai-block.component.html',
  styleUrls: ['./ai-block.component.scss']
})
export class AiBlockComponent {
  @Input() title = "No prompt";
  @Input() prompt = "Print literally 'Error: No prompt!'";

  resultPending = false;
  resultAvailable = false;
  result = "";

  constructor(private dataService: CoreService, private http: HttpClient) {}

  sendPrompt() {
    this.resultPending = true;
    this.http.post(environment.apiRoot + "/api/ai/completion", {
      prompt: this.prompt
    }, {responseType: "text"}).subscribe(result => {
      this.resultPending = false;
      this.resultAvailable = true;
      this.result = result;
    });
  }
}
