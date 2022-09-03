import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { PortionsOverviewComponent } from './dictionary/portions-overview/portions-overview.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatToolbarModule} from "@angular/material/toolbar";
import {RouterModule} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatRippleModule} from "@angular/material/core";
import {MatTooltipModule} from "@angular/material/tooltip";
import { VocabularyViewComponent } from './dictionary/vocabulary-view/vocabulary-view.component';
import { SearchComponent } from './dictionary/search/search.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { ChatViewComponent } from './trainer/chat-sessions/chat-view/chat-view.component';
import { ChatTrainerSessionComponent } from './trainer/chat-sessions/chat-trainer-session/chat-trainer-session.component';
import { TrainerLauncherComponent } from './trainer/trainer-launcher/trainer-launcher.component';

@NgModule({
  declarations: [
    AppComponent,
    PortionsOverviewComponent,
    VocabularyViewComponent,
    SearchComponent,
    ChatViewComponent,
    ChatTrainerSessionComponent,
    TrainerLauncherComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([
      {path: "", redirectTo: "/dictionary", pathMatch: "full"},
      {path: "dictionary", component: PortionsOverviewComponent},
      {path: "dictionary/:vocabulary", component: VocabularyViewComponent},
      {path: "search", component: SearchComponent},
      {path: "trainer", component: TrainerLauncherComponent}
    ]),
    HttpClientModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatListModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatRippleModule,
    MatTooltipModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
