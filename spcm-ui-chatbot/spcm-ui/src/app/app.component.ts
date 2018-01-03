import { Component, OnInit} from '@angular/core';
import { User } from './models/user';
import {trigger, state, style, transition, animate} from '@angular/core';
import { Location } from '@angular/common';
import { ConfigService } from './services/config.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'SPC Mobile';
  ngOnInit() {

  }
  constructor(private location: Location, private configService: ConfigService) {}

   public menuItemsArray: any[] = [
     { 'title': 'Dashboard', 'link': '/dashboard' },
     { 'title': 'Agreements', 'link': '/agreements' },
     { 'title': 'Calendar', 'link': '/calendar' },
     { 'title': 'Help', 'link': '/help' },
  ];
   public getLocation() {
     return this.location;
   }
   public onMenuClose() {
     console.log('menu closed');
   }
   public onMenuOpen() {
     console.log('menu Opened');
   }
   private onItemSelect(item: any) {
     console.log(item.title);
   }
   public isLoggedIn(){
     //console.log(this.configService.getUser());
     //return this.configService.hasValidToken();
     return true;

   }

  goBack(): void {
   this.location.back();
  }


}
