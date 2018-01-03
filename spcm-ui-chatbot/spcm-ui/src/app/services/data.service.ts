import { Injectable } from '@angular/core';
import { Http, Request, Response, RequestOptions, Headers, RequestMethod } from '@angular/http';
import { Router } from '@angular/router';
import 'rxjs/RX';
// import 'rxjs/add/operator/map';
import { ConfigService } from './config.service';

/*
Usage:
1. inject this service to your component.
2. when need to call api end point with GET method:
    configService.doGet(endPointURI,
      resultData => {
        // process resultData. resultData is responseJson.data
      }
      // optionally
      , (rawJson) => {
        // process rawJson. rawJson is full responseJson
      }
    )
3. when need to call api end point with POST method:
    configService.doPost(endPointURI, postData,
      // same as doGet
*/
@Injectable()
export class DataService {

  constructor(public configService: ConfigService, private router: Router, public http: Http) {
    this.configService = configService;
  }

  private log(message: string): void {
    console.log(message);
  }
  private ensureHttpHeaders(): RequestOptions {
    const headers = new Headers();
    headers.set('Content-Type', 'application/json');
    headers.set('Accept', 'application/json');
    headers.append(this.configService.securityTokenName, this.configService.securityToken);
    const opts = new RequestOptions();
    opts.headers = headers;
    return opts;
  }
  private processAPIResult(response: Response, onSuccessFunction: Function, onFailFunction?: Function) {
    const jsonData = response.json();
    if (jsonData) {
      if (jsonData.status === 'SUCCESS') {
        try {
          onSuccessFunction(jsonData.data);
        } catch ( err ) {
          this.log('error in calling onSuccessFunction, ' + err);
        }
      } else {
        if (onFailFunction) {
          try {
            onFailFunction(jsonData);
          } catch ( err ) {
            this.log('error in calling onFailFunction, ' + err);
          }
        } else {
          this.log(`http call returned, API status: ${jsonData.status}, code: ${jsonData.code}, message: ${jsonData.message}`);
        }
      }
    }
  }
  private callingErrorHandler(response: Response) {
    this.log(`Error in http call, status: ${response.status}`);
    // to do:
    // handle 401, 498 anthentication error
    if (response.status === 401 || response.status === 498) {
      this.log(`navigate to login page`);
      this.router.navigate(['login']);
    }
    // handle 400, 406 request data error
    // handle 500      server error
  }


  doGET(uri: string, onSuccessFunction: Function, onFailFunction?: Function) {
    const url = `${this.configService.apiRoot}/${uri}`;
    this.log('GET ' + url);
    this.http.get(url, this.ensureHttpHeaders())
    .toPromise()
    .then(response => {
      this.processAPIResult(response, onSuccessFunction, onFailFunction);
    })
    .catch(response => {
      this.callingErrorHandler(response);
    });
  }

  doPOST(uri: string, postData: any, onSuccessFunction: Function, onFailFunction?: Function) {
    const url = `${this.configService.apiRoot}/${uri}`;
    this.log('POST ' + url);
    this.http.post(url, postData, this.ensureHttpHeaders())
    .toPromise()
    .then(response => {
      this.processAPIResult(response, onSuccessFunction, onFailFunction);
    })
    .catch(response => {
      this.callingErrorHandler(response);
    });
  }

  // caller can use following ways to process result:
  // httpCallAsPromise(...).subscribe(res => this.log(res.json()));
  // httpCallAsPromise(...).subscribe(
  //    data => self.data = data, //success
  //    error => this.log('error', error),
  //    () => {this.log('call finished')}
  //  )
  httpCallAsPromise(method: string, uri: string, paramData: any) {
    this.log('HTTP ${method}, ${uri}');
    const url = `${this.configService.apiRoot}/${uri}`;
    const reqOptions = this.ensureHttpHeaders();
    reqOptions.url = url;
    reqOptions.method = RequestMethod[method.toUpperCase()];
    if (paramData) {
      reqOptions.body = paramData;
    }
    return this.http.request(new Request(reqOptions)).map((res: Response) => res.json());
  }

}
