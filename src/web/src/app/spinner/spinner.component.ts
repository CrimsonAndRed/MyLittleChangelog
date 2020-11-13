import { SpinnerService } from './spinner.service'
import { Component } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
    selector: 'spinner',
    templateUrl: 'spinner.component.html',
    styleUrls: ['spinner.component.scss']
})
export class Spinner implements OnInit, OnDestroy {

    spinning: boolean = false;
    subscription: Subscription = null;

    constructor(private spinnerService: SpinnerService) {}

    ngOnInit() {
      this.subscription = this.spinnerService.subscribe((val) => this.spinning = val);
      this.spinning = this.spinnerService.isSpinning();
    }

    ngOnDestroy() {
      if (this.subscription) {
        this.subscription.unsubscribe();
      }
    }
}
