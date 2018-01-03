import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { AgreementComponent } from './components/agreements/agreement.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { AppRoutingModule } from './app-routing.module';
import { SlideMenuModule } from './cuppa-ng2-slidemenu/cuppa-ng2-slidemenu';
import { ScheduleModule } from 'primeng/primeng';
import { HttpModule } from '@angular/http';
import { ConfigService } from './services/config.service';
import { DataService } from './services/data.service';
import { HelpComponent } from './components/help/help.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    DashboardComponent,
    AgreementComponent,
    CalendarComponent,
    HelpComponent
  ],
  imports: [
    NgbModule.forRoot(),
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SlideMenuModule,
    ScheduleModule,
    HttpModule,
    FormsModule
  ],
  providers: [ ConfigService, DataService],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
