import { Component, Input } from '@angular/core';

import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { NewGroupWithId } from 'app/model/new-group';
import { NewLeafWithId } from 'app/model/new-leaf';

@Component({
  selector: 'group-content',
  templateUrl: './group-content.component.html',
  styleUrls: ['./group-content.component.scss']
})
export class GroupContentComponent {

  @Input() groupContent: GroupContent;

  constructor() {
  }

  onNewGroupCreated(newGroupWithId: NewGroupWithId) {
    const newGroup: GroupContent = {
      id: newGroupWithId.id,
      name: newGroupWithId.name,
      vid: newGroupWithId.vid,
      groupContent: [],
      leafContent: [],
    };
    this.groupContent.groupContent.push(newGroup);
  }

  onNewLeafCreated(newLeafWithId: NewLeafWithId) {
    const newLeaf: LeafContent = {
      id: newLeafWithId.id,
      name: newLeafWithId.name,
      vid: newLeafWithId.vid,
      valueType: newLeafWithId.valueType,
      value: newLeafWithId.value,
    }
    this.groupContent.leafContent.push(newLeaf);
  }

}
