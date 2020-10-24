import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'expand-block',
  templateUrl: './expand-block.component.html',
  styleUrls: ['./expand-block.component.scss']
})
export class ExpandBlockComponent {

  @Input() isContentShowed: boolean;
  @Output() isContentShowedChange = new EventEmitter<boolean>();
  @Output() globalShowChange = new EventEmitter<boolean>();

  constructor() { }

  onSingleShowChange(value: boolean): void {
    this.isContentShowed = value;
    this.isContentShowedChange.emit(this.isContentShowed);
  }

  onGlobalShowChange(value: boolean): void {
    this.isContentShowed = value;
    this.globalShowChange.emit(this.isContentShowed);
  }

}
