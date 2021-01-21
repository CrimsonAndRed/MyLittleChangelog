import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'expand-all-button',
  templateUrl: './expand-all-button.component.html',
  styleUrls: ['./expand-all-button.component.scss']
})
export class ExpandAllButtonComponent {

  @Input() isContentShowed: boolean;
  @Output() isContentShowedChange = new EventEmitter<boolean>();

  constructor() { }

  onClick(): void {
    this.isContentShowed = !this.isContentShowed;
    this.isContentShowedChange.emit(this.isContentShowed);
  }

}
