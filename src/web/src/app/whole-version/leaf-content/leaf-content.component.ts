import { Component, Input } from '@angular/core';

import { LeafContent } from 'app/model/leaf-content';

@Component({
  selector: 'leaf-content',
  templateUrl: './leaf-content.component.html',
  styleUrls: ['./leaf-content.component.scss']
})
export class LeafContentComponent {

  @Input() leafContent: LeafContent;

  constructor() {
  }

}
