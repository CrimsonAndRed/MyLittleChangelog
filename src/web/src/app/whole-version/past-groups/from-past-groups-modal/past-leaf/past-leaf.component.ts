import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PastRadioEvent } from 'app/model/past-radio-event'

import { LeafContent } from 'app/model/leaf-content';

@Component({
  selector: 'past-leaf',
  templateUrl: './past-leaf.component.html',
  styleUrls: ['./past-leaf.component.scss']
})
export class PastLeaf {

  @Input() leafContent: LeafContent;

  @Input() parentId: number;

  @Output() onRadioChange = new EventEmitter<PastRadioEvent>();

  constructor() {
  }

  onRadioButtonClick() {
    this.onRadioChange.emit({ value: this.leafContent, kind: "leaf", parentId: this.parentId });
  }
}
