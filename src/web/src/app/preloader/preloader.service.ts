import { Injectable } from '@angular/core';
import { Observable, Subject, Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class PreloaderService {
    private loading: boolean = false;
    private subject: Subject<boolean> = new Subject();

    constructor() {}

    public start() {
      this.loading = true;
      this.messageSubscribers();
    }

    public stop() {
      this.loading = false;
      this.messageSubscribers();
    }

    public isLoading(): boolean {
      return this.loading;
    }

    public subscribe(fn: (val: boolean) => void): Subscription {
      return this.subject.subscribe({
        next: fn
      });
    }

    public wrap(obs: Observable<any>) {
      this.start();
      obs
        .pipe(
          finalize(() => this.stop())
        )
        .subscribe()
    }

    private messageSubscribers() {
      this.subject.next(this.loading);
    }
}
