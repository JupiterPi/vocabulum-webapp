import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {
  showLoop = false;

  ngOnInit(): void {
    setTimeout(() => this.showLoop = true, 2500);
  }
}
