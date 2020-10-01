import { Component, Input } from '@angular/core';

import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'group-content',
  templateUrl: './group-content.component.html',
  styleUrls: ['./group-content.component.scss']
})
export class GroupContentComponent {

  @Input() groupContent: GroupContent;

  constructor() {
  }

}
