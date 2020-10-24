import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PastGroupContent, PastRadioEvent } from 'app/previous-version/previous-version.model';

@Component({
  selector: 'past-group',
  templateUrl: './past-group.component.html',
  styleUrls: ['./past-group.component.scss']
})
export class PastGroup {

  @Input() groupContent: PastGroupContent;

  @Input() parentId: number;

  @Output() onRadioChange = new EventEmitter<PastRadioEvent>();

  constructor() {
  }

  onSubRadioChange(event: PastRadioEvent) {
    this.onRadioChange.emit(event)
  }

  onRadioButtonClick() {
    this.onRadioChange.emit({ value: this.groupContent, kind: "group", parentId: this.parentId });
  }
}
