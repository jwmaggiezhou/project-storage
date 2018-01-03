import { Component, OnInit, AfterViewInit, ElementRef} from '@angular/core';
import {trigger, state, style, transition, animate} from '@angular/core';
import { Location} from '@angular/common';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements AfterViewInit {

  constructor(private elementRef: ElementRef) { }
  ngAfterViewInit() {
    this.elementRef.nativeElement.ownerDocument.body.style.backgroundColor = '#fff';
  }

}
