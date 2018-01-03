import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { AgreementComponent } from './components/agreements/agreement.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { HelpComponent } from './components/help/help.component';
import { HomeComponent } from './components/home/home.component';


const appRoutes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full'},
  { path: 'dashboard', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  { path: 'agreements', component: AgreementComponent },
  { path: 'calendar', component: CalendarComponent },
  { path: 'help', component: HelpComponent },
  { path: 'home', component: HomeComponent },
];

@NgModule({
  imports: [ RouterModule.forRoot(appRoutes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
