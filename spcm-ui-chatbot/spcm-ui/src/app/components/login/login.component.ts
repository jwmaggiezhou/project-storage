import { Component, AfterViewInit, ElementRef } from '@angular/core';
import { ConfigService } from '../../services/config.service';
import { User } from '../../models/user';
import { Router } from '@angular/router';
import { DataService } from '../../services/data.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [ DataService ]
})
export class LoginComponent implements AfterViewInit {
  public username: string;
  public password: string;
  constructor(
    private configService: ConfigService,
    private dataService: DataService,
    private elementRef: ElementRef,
    private router: Router) { }
  ngAfterViewInit() {
    // reset login status
    this.elementRef.nativeElement.ownerDocument.body.style.backgroundColor = '#0071A9';
  }
  public login() {


    // make a post request to the api
    console.log(`logging in user ${this.username}`);
    this.dataService.doPOST('api/logon', {username: this.username, password: this.password},
      data => {
        this.password = '';
        //console.log(data['user']);
        this.configService.processUserLogin(data);
        //console.log('set user success');
        this.router.navigate(['home']);
      },
      () => { console.log('login failed'); });
  }

}
