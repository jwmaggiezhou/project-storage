import { Component, OnInit, NgModule, OnChanges, ViewEncapsulation, Input, Output} from '@angular/core';
import { EventEmitter, ElementRef, AfterViewInit, Pipe, PipeTransform } from '@angular/core';
import { SafeResourceUrl, DomSanitizer } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { ClickOutsideDirective } from './clickOutside';
import { Router, RouterModule } from '@angular/router';
import { Location } from '@angular/common';
import { ConfigService } from '../../../../services/config.service';
import { User } from '../../../../models/user';

@Component({
  selector: 'cuppa-slidemenu',
  templateUrl: 'slidemenu.template.html',
  styleUrls: ['slidemenu.styles.scss'],

})

export class SlideMenu implements AfterViewInit{

    @Input() menulist: any;
    @Output('open')
    open: EventEmitter<number> = new EventEmitter<number>();
    @Output('close')
    close: EventEmitter<number> = new EventEmitter<number>();
    @Output('onItemSelect')
    itemSelect: EventEmitter<number> = new EventEmitter<number>();
    public menuState: boolean;
    private targetElement: any;
    /*private overlayElem: any;*/

    ngOnInit() {
      this.menuState = false;
   }

    constructor(
      private configService: ConfigService,
      private _elementRef: ElementRef,
      private sanitizer: DomSanitizer,
      private location: Location,
      private router: Router) {
      /*  this.addOverlayElement();*/
    }
    user = this.configService.getUser();

    ngAfterViewInit() {}

    public menuToggle() {
        this.menuState = !this.menuState;
        /*this.toggleOverlay();*/
        if (this.menuState) {
            this.open.emit();
        } else {
            this.close.emit();
        }
    }
    public closeMenu() {
         this.menuState = false;
         /*this.overlayElem.style['opacity'] = 0;*/
    }
    public onItemClick(item: any) {
        this.itemSelect.emit(item);
        setTimeout(() => this.closeMenu(), 100);

    }

    public logout() {
      this.configService.clearUser();
      this.router.navigate(['login']);
    }

    /*
    private addOverlayElement(){
        this.overlayElem = document.createElement('div');
        this.overlayElem.classList.add('cuppa-menu-overlay');
        this.overlayElem.style['position'] = 'fixed';
        this.overlayElem.style['background'] = 'rgba(0, 0, 0, 0.7)';
        this.overlayElem.style['top'] = 0;
        this.overlayElem.style['left'] = 0;
        this.overlayElem.style['right'] = 0;
        this.overlayElem.style['bottom'] = 0;
        this.overlayElem.style['opacity'] = 0;
        this.overlayElem.style['pointer-events'] = 'none';
        this.overlayElem.style['transition'] = 'all .2s linear';
        document.getElementsByTagName('body')[0].appendChild(this.overlayElem);
    }
    private toggleOverlay(){
        if(this.overlayElem.style['opacity'] == 0){
            this.overlayElem.style['opacity'] = 1;
        }
        else if(this.overlayElem.style['opacity'] == 1){
            this.overlayElem.style['opacity'] = 0;
        }
    }*/
 }
@NgModule({
  imports:      [ CommonModule, RouterModule],
  declarations: [ SlideMenu, ClickOutsideDirective ],
  exports:      [ SlideMenu, ClickOutsideDirective ]
})
export class SlideMenuModule { }
