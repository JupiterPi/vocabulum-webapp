import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {
  @Output("ready") ready = new EventEmitter<void>();

  showLoop = false;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    setTimeout(() => this.showLoop = true, 2500);
    this.tryConnect();
  }

  failedConnects = 0;
  tryConnect() {
    this.http.get(environment.apiRoot + "/ping", {responseType: "text"}).subscribe(
      () => {
        if (this.failedConnects > 0) {
          window.location.reload();
        } else {
          this.ready.emit();
        }
      },
      () => {
        this.failedConnects++;
        if (this.failedConnects <= 7) {
          setTimeout(() => {
            this.tryConnect();
          }, 2000);
        }
      }
    );
  }
}
