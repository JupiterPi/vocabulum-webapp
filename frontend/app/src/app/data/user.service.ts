import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

export type UserDetails = {
  username: string,
  email: string
};

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  devUserDetails: UserDetails = {
    username: "JupiterPi",
    email: "jupiterpi@vocabulum.de"
  };

  getUserDetails() {
    return this.devUserDetails;
  }
}
