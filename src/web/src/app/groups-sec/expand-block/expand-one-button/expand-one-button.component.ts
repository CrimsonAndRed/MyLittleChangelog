import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'expand-one-button',
  templateUrl: './expand-one-button.component.html',
  styleUrls: ['./expand-one-button.component.scss']
})
export class ExpandOneButtonComponent {

  @Input() isContentShowed: boolean;
  @Output() isContentShowedChange = new EventEmitter<boolean>();

  constructor() { }

  onSwitchContendShowedClick(): void {
    this.isContentShowed = !this.isContentShowed;
    this.isContentShowedChange.emit(this.isContentShowed);
  }

}
