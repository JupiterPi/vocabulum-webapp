import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './ui/app.component';
import { PortionsOverviewComponent } from './ui/dictionary/portions-overview/portions-overview.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatToolbarModule} from "@angular/material/toolbar";
import {RouterModule} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatRippleModule} from "@angular/material/core";
import {MatTooltipModule} from "@angular/material/tooltip";
import { VocabularyViewComponent } from './ui/dictionary/vocabulary-view/vocabulary-view.component';
import { SearchComponent } from './ui/dictionary/search/search.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { ChatViewComponent } from './ui/trainer/chat-sessions/chat-view/chat-view.component';
import { ChatTrainerSessionComponent } from './ui/trainer/chat-sessions/chat-trainer-session/chat-trainer-session.component';
import { TrainerLauncherComponent } from './ui/trainer/trainer-launcher/trainer-launcher.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { LoginComponent } from './ui/account/login/login.component';
import { RegisterComponent } from './ui/account/register/register.component';
import { TranslationAssistanceComponent } from './ui/translation-assistance/translation-assistance.component';
import { ProfileComponent } from './ui/account/profile/profile.component';
import { HistoryComponent } from './ui/account/history/history.component';
import { PlansComponent } from './ui/account/plans/plans.component';
import { CardsTrainerSessionComponent } from './ui/trainer/cards-trainer-session/cards-trainer-session.component';
import {FaIconLibrary, FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {
  faBook,
  faChevronLeft,
  faComments,
  faDisplay, faEye,
  faPaperPlane,
  faWindowRestore
} from "@fortawesome/free-solid-svg-icons";
import {MatTreeModule} from "@angular/material/tree";
import { VocabularySelectorComponent } from './ui/trainer/vocabulary-selector/vocabulary-selector.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import { ConfirmEmailComponent } from './ui/account/confirm-email/confirm-email.component';
import { DiscordBannerComponent } from './ui/account/profile/discord-banner/discord-banner.component';
import {CookieModule, CookieService} from "ngx-cookie";

@NgModule({
  declarations: [
    AppComponent,
    PortionsOverviewComponent,
    VocabularyViewComponent,
    SearchComponent,
    ChatViewComponent,
    ChatTrainerSessionComponent,
    TrainerLauncherComponent,
    LoginComponent,
    RegisterComponent,
    TranslationAssistanceComponent,
    ProfileComponent,
    HistoryComponent,
    PlansComponent,
    CardsTrainerSessionComponent,
    VocabularySelectorComponent,
    ConfirmEmailComponent,
    DiscordBannerComponent
  ],
    imports: [
        BrowserModule,
        RouterModule.forRoot([
            {path: "", redirectTo: "/dictionary", pathMatch: "full"},
            /*{path: "", component: PortionsOverviewComponent},*/
            {path: "login", component: LoginComponent},
            {path: "register", component: RegisterComponent},
            {path: "confirmEmail", component: ConfirmEmailComponent},
            {path: "my", component: ProfileComponent},
            {path: "dictionary", component: PortionsOverviewComponent},
            {path: "dictionary/:vocabulary", component: VocabularyViewComponent},
            {path: "search", component: SearchComponent},
            {path: "trainer", component: TrainerLauncherComponent},
            {path: "trainer/chat", component: ChatTrainerSessionComponent},
            {path: "trainer/cards", component: CardsTrainerSessionComponent},
            {path: "translationAssistance", component: TranslationAssistanceComponent}
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
        FormsModule,
        ServiceWorkerModule.register('ngsw-worker.js', {
            enabled: environment.production,
            // Register the ServiceWorker as soon as the application is stable
            // or after 30 seconds (whichever comes first).
            registrationStrategy: 'registerWhenStable:30000'
        }),
        FontAwesomeModule,
        MatTreeModule,
        MatCheckboxModule,
        CookieModule.withOptions()
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(fontawesome: FaIconLibrary) {
    fontawesome.addIcons(
      faBook, faComments, faDisplay, faWindowRestore, faChevronLeft, faPaperPlane, faEye
    );
  }
}
