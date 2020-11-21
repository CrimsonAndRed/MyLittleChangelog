import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'move-updown-button',
  templateUrl: './move-updown-button.component.html',
  styleUrls: ['./move-updown-button.component.scss']
})
export class MoveUpDownButtonComponent {

  @Input() upShowCondition: boolean;
  @Input() downShowCondition: boolean;

  @Output() up = new EventEmitter<void>();
  @Output() down = new EventEmitter<void>();

  constructor() { }

  handleUpButtonClick(): void {
    this.up.emit();
  }

  handleDownButtonClick(): void {
    this.down.emit();
  }
}
