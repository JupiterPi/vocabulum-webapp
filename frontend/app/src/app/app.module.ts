import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './ui/app.component';
import { PortionsOverviewComponent } from './ui/dictionary/portions-overview/portions-overview.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import {RouterModule} from "@angular/router";
import {MatIconModule} from "@angular/material/icon";
import {MatRippleModule} from "@angular/material/core";
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
  faWindowRestore,
  faFileLines,
  faPlay,
  faRotateLeft,
} from "@fortawesome/free-solid-svg-icons";
import {MatTreeModule} from "@angular/material/tree";
import { VocabularySelectorComponent } from './ui/trainer/vocabulary-selector/vocabulary-selector.component';
import { DiscordBannerComponent } from './ui/account/profile/discord-banner/discord-banner.component';
import {CookieModule} from "ngx-cookie";
import {AdminOverviewComponent} from './admin/ui/admin-overview/admin-overview.component';
import { VoucherComponent } from './ui/account/voucher/voucher.component';
import { VouchersPrintableComponent } from './admin/ui/vouchers-printable/vouchers-printable.component';
import { TestTrainerComponent } from './ui/trainer/test-trainer/test-trainer.component';
import {
  VkButtonComponent,
  VkButtonsComponent,
  VkPageComponent,
  VkSectionComponent,
  VkSectionHeaderComponent,
  VkMetaComponent,
  VkMetaKeyComponent,
  VkMetaValueComponent, VkMetaContainerComponent, VkInput,
} from './components/vk-components';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatButtonModule} from "@angular/material/button";
import {MatTooltipModule} from "@angular/material/tooltip";
import { AiBlockComponent } from './ui/ai/ai-block/ai-block.component';
import { initializeApp,provideFirebaseApp } from '@angular/fire/app';
import { provideAuth,getAuth } from '@angular/fire/auth';
import { VoucherTerminalComponent } from './admin/ui/voucher-terminal/voucher-terminal.component';

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
    DiscordBannerComponent,
    AdminOverviewComponent,
    VoucherComponent,
    VouchersPrintableComponent,
    TestTrainerComponent,
    VkPageComponent,
    VkSectionComponent,
    VkSectionHeaderComponent,
    VkButtonsComponent,
    VkButtonComponent,
    VkMetaContainerComponent,
    VkMetaComponent,
    VkMetaKeyComponent,
    VkMetaValueComponent,
    VkInput,
    AiBlockComponent,
    VoucherTerminalComponent,
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([
      {path: "", redirectTo: "/dictionary", pathMatch: "full"},

      {path: "login", component: LoginComponent},
      {path: "register", component: RegisterComponent},
      {path: "my", component: ProfileComponent},
      {path: "pro", redirectTo: "/my", pathMatch: "full"},
      {path: "voucher", component: VoucherComponent},

      {path: "dictionary", component: PortionsOverviewComponent},
      {path: "dictionary/:vocabulary", component: VocabularyViewComponent},
      {path: "search", component: SearchComponent},

      {path: "trainer", component: TrainerLauncherComponent},
      {path: "trainer/chat", component: ChatTrainerSessionComponent},
      {path: "trainer/cards", component: CardsTrainerSessionComponent},
      {path: "trainer/test", component: TestTrainerComponent},

      {path: "translationAssistance", component: TranslationAssistanceComponent},

      {path: "admin", component: AdminOverviewComponent},
      {path: "admin/printVouchers", component: VouchersPrintableComponent},
      {path: "admin/terminal", component: VoucherTerminalComponent},
    ]),
    HttpClientModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatRippleModule,
    FormsModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
      // Register the ServiceWorker as soon as the application is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
    FontAwesomeModule,
    MatTreeModule,
    CookieModule.withOptions(),
    MatCheckboxModule,
    MatButtonModule,
    MatTooltipModule,
    provideFirebaseApp(() => initializeApp(environment.firebase)),
    provideAuth(() => getAuth()),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(fontawesome: FaIconLibrary) {
    fontawesome.addIcons(
      faBook, faComments, faDisplay, faWindowRestore, faChevronLeft, faPaperPlane, faEye, faFileLines, faPlay, faRotateLeft
    );
  }
}
