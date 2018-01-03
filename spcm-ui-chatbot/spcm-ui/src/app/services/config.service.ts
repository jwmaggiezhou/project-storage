import { Injectable } from '@angular/core';
import { User } from '../models/user';


@Injectable()
export class ConfigService {

  readonly apiRoot = 'http://10.200.19.171:9080/spcm'; // api server base URL
  readonly securityTokenName = 'SPC_TOKEN';

  // Login component will set following fields
  currentUser: User;
  securityToken: string;
  tokenExpireTime: Date;
  preferredLangCode = 'en';

  constructor() { this.init(); }

  private init() {
    // load from storage
    const userLoginStr = localStorage.getItem('SPC.token');
    if (userLoginStr) {
      console.log('Loaded SPC.token from storage: ' + userLoginStr);
      const storedUserLogin = JSON.parse(userLoginStr);
      if (storedUserLogin) {
        this.securityToken = storedUserLogin.securityToken;
        this.tokenExpireTime = new Date(storedUserLogin.tokenExpireTime);
        this.preferredLangCode = storedUserLogin.preferredLangCode;
      }
    }
  }

  hasValidToken(): boolean {
    return this.securityToken && this.tokenExpireTime != null
      && this.tokenExpireTime.getTime() > (new Date).getTime();
  }

  processUserLogin(userLogin: any) {
    this.currentUser = userLogin.user;
    this.preferredLangCode = this.currentUser && this.currentUser.defaultLocale === 'fr' ? 'fr' : 'en';

    this.securityToken = userLogin.securityToken;
    this.tokenExpireTime = new Date(userLogin.tokenExpireTime);

    userLogin.user = null;
    localStorage.setItem('SPC.token', JSON.stringify({
      securityToken: this.securityToken,
      tokenExpireTime: this.tokenExpireTime,
      preferredLangCode: this.preferredLangCode
    }));
    console.log('Stored SPC.token to storage.');
  }

  public clearUser() {
    this.currentUser = null;
    this.securityToken = null;
    this.tokenExpireTime = null;
    localStorage.removeItem('SPC.token');
  }

  public getUser() {
    return this.currentUser;
  }

}
