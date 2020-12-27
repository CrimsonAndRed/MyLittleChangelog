import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'base-header',
  templateUrl: './base-header.component.html',
  styleUrls: ['./base-header.component.scss']
})
export class BaseHeaderComponent {

  @Output() nodeChecked = new EventEmitter<void>();

  constructor() { }

  onRadioCheck(): void {
    this.nodeChecked.emit();
  }

}
