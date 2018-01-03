import { Component, OnInit, AfterViewInit, ElementRef, ViewChild} from '@angular/core';
import {ScheduleModule} from 'primeng/primeng';
import {DataService} from '../../services/data.service';
//import {ConfigService} from '../../services/config.service';
//import { CalenderEvent } from '../../models/calender-events';
//import { User } from '../../models/user';
declare var $: any;


@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css'],
  providers:[DataService]
})
export class CalendarComponent implements OnInit {
  constructor(
    private dataService: DataService
  ) { }
  options:{};
  //events:any[];
  eventData:any[] = [];
  headerConfig:{};
  event: CalenderEvent;
  eventList: CalenderEvent[];


  ngOnInit() {

      this.headerConfig = {
        left: "",
        center: "title",
        right: "",
      };

      this.loadData(this.eventData, '');
  }



  private getCurrentMonth() {
    var today = new Date();
    var yyyy = today.getFullYear().toString();
    var mm = '';
    var month = today.getMonth() + 1;
    if( month < 10 ){
      var mm = '0'+ month.toString();
    } else {
      var mm = month.toString();
    }
    return yyyy+mm;
  }

    public loadData(container, request){
      //make a get request to the api
      if (request == ''){
        var apiRequest = 'api/data/calendars/'+this.getCurrentMonth();
      } else {
        var apiRequest = 'api/data/calendars/'+request;
      }
      console.log(apiRequest);
      this.dataService.doGET(apiRequest,
        data => {
            for(let item of data){
              //console.log(item);
              container.push(item)
            }
            //this.events = this.parseToEvent(container);
            this.parseToEvent(container);
        },
        () => { console.log("loading failed"); });
    }

    handleDayClick(e) {
      this.eventList = [];

      for (let event of this.eventData){

          if(event.date == e.date.format()){
            this.eventList.push(event);
          }
      };
      if(this.eventList.length == 0){
        let noEvent = new CalenderEvent();
        noEvent.event = "No Event";
        this.eventList.push(noEvent);
      };


    }


    parseToEvent(eventData) {
      let dateCollect = [];
      for (let event of eventData){
          dateCollect.push(event.date);
          $("td[data-date='"+event.date+"']").addClass('activeDay');
      };

    }

    back(fc) {
      fc.prev();
      let newRequest = fc.getDate().format().split("T")[0].replace("-","").split("-")[0];
      this.eventData = [];
      this.loadData(this.eventData, newRequest);
    }

    next(fc) {
      fc.next();
      let newRequest = fc.getDate().format().split("T")[0].replace("-","").split("-")[0];
      this.eventData = [];
      this.loadData(this.eventData, newRequest);
    }



}
export class CalenderEvent {
  id: number;
  date: string;
  event: string;
  isManualCalendar: boolean;

}
