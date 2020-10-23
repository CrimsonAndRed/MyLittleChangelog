import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'expand-all-button',
  templateUrl: './expand-all-button.component.html',
  styleUrls: ['./expand-all-button.component.scss']
})
export class ExpandAllButtonComponent {

  @Input() isContentShowed: boolean;
  @Output() onSwitchExpandAll = new EventEmitter<void>();

  constructor() { }

  onClick() {
    this.isContentShowed = !this.isContentShowed;
    this.onSwitchExpandAll.emit();
  }

}
