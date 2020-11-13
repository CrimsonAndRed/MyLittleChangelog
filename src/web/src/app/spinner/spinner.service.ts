import { Injectable } from '@angular/core';
import { Observable, Subject, Subscription } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class SpinnerService {
    private spin: boolean = false;
    private subject: Subject<boolean> = new Subject();

    constructor() {}

    public startSpin() {
      this.spin = true;
      this.messageSubscribers();
    }

    public stopSpin() {
      this.spin = false;
      this.messageSubscribers();
    }

    public isSpinning(): boolean {
      return this.spin;
    }

    public subscribe(fn: (val: boolean) => void): Subscription {
      return this.subject.subscribe({
        next: fn
      });
    }

    public wrapSpinner(obs: Observable<any>) {
      this.startSpin();
      obs
        .subscribe(() => this.stopSpin())
    }

    private messageSubscribers() {
      this.subject.next(this.spin);
    }
}
