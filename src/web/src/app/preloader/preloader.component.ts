import { PreloaderService } from './preloader.service'
import { Component } from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
    selector: 'preloader',
    templateUrl: 'preloader.component.html',
    styleUrls: ['preloader.component.scss']
})
export class Preloader implements OnInit, OnDestroy {

    loading: boolean = false;
    subscription: Subscription = null;

    constructor(private preloaderService: PreloaderService) {}

    ngOnInit() {
      this.subscription = this.preloaderService.subscribe((val) => this.loading = val);
      this.loading = this.preloaderService.isLoading();
    }

    ngOnDestroy() {
      if (this.subscription) {
        this.subscription.unsubscribe();
      }
    }
}
