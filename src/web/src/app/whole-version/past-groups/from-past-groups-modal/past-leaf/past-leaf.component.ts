import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PastLeafContent, PastRadioEvent } from 'app/previous-version/previous-version.model';

@Component({
  selector: 'past-leaf',
  templateUrl: './past-leaf.component.html',
  styleUrls: ['./past-leaf.component.scss']
})
export class PastLeaf {

  @Input() leafContent: PastLeafContent;

  @Input() parentId: number;

  @Output() onRadioChange = new EventEmitter<PastRadioEvent>();

  constructor() {
  }

  onRadioButtonClick() {
    this.onRadioChange.emit({ value: this.leafContent, kind: "leaf", parentId: this.parentId });
  }
}
