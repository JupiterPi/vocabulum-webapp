import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, filter, Observable} from "rxjs";
import {SessionService} from "./session.service";
import {environment} from "../environments/environment";
import {isNonNull} from "../util";

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  constructor(private session: SessionService, private http: HttpClient) {
    this.session.getAuthHeaders().subscribe(authHeaders => {
      this.http.get<any>(environment.apiRoot + "/settings", authHeaders)
        .subscribe(settings => this.settings$.next(settings));
    });
  }

  settings$ = new BehaviorSubject<any | null>(null);

  getSetting(key: string): Observable<any> {
    return new Observable<any>(subscriber => {
      this.settings$.pipe(filter(isNonNull)).subscribe(settings => {
        subscriber.next(settings[key]);
      });
    });
  }

  updateValue(key: string, value: string | boolean) {
    this.session.getAuthHeaders().subscribe(authHeaders => {
      this.http.put<any>(environment.apiRoot + "/settings", { [key]: value }, authHeaders)
        .subscribe(settings => this.settings$.next(settings));
    });
  }

  resetSettings() {
    this.session.getAuthHeaders().subscribe(authHeaders => {
      this.http.delete<any>(environment.apiRoot + "/settings", authHeaders)
        .subscribe(settings => this.settings$.next(settings));
    });
  }
}
