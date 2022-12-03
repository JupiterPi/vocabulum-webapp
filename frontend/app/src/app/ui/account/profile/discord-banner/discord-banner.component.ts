import {Component} from '@angular/core';

@Component({
  selector: 'app-discord-banner',
  templateUrl: './discord-banner.component.html',
  styleUrls: ['./discord-banner.component.scss']
})
export class DiscordBannerComponent {
  openDiscord() {
    const w = window.open("https://discord.gg/49P3pFA5a8", "_blank") as Window;
    if (w != null) w.focus();
  }
}
