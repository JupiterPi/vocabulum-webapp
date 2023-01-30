import {Component, Input} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DataService} from "../../../data/data.service";

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

  constructor(private dataService: DataService, private http: HttpClient) {}

  sendPrompt() {
    this.resultPending = true;
    this.http.post(this.dataService.backendRoot + "/api/ai/completion", {
      prompt: this.prompt
    }, {responseType: "text"}).subscribe(result => {
      this.resultPending = false;
      this.resultAvailable = true;
      this.result = result;
    });
  }
}
