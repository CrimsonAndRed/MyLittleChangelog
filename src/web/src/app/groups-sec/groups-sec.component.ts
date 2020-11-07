import { Component, Input, ViewChildren } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { GroupLeavesSecComponent } from './group-leaves-sec/group-leaves-sec.component';

import {
  GroupsSecConfig,
  GroupsSecContext,
} from './groups-sec.model';

@Component({
  selector: 'groups-sec',
  templateUrl: './groups-sec.component.html',
  styleUrls: ['./groups-sec.component.scss']
})
export class GroupsSecComponent {

  @Input() groups: GroupContent[];
  @Input() config: GroupsSecConfig;
  @Input() context: GroupsSecContext;

  @ViewChildren(GroupLeavesSecComponent) embeddedGroupLeaves: GroupLeavesSecComponent[];

  isContentShowed = false;

  constructor() { }

  changeGlobalContentShow(value: boolean): void {
    this.isContentShowed = value;
    if (value === true) {
      this.embeddedGroupLeaves.forEach(egl => egl.changeGlobalContentShow(true));
    }
  }

}
