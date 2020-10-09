import { Component, Input } from '@angular/core';

import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { NewGroupWithId } from 'app/model/new-group';
import { NewLeafWithId } from 'app/model/new-leaf';
import { UpdatedLeaf } from 'app/model/update-leaf';
import { UpdatedGroup } from 'app/model/update-group';

@Component({
  selector: 'groups-leaves',
  templateUrl: './groups-leaves.component.html',
  styleUrls: ['./groups-leaves.component.scss']
})
export class GroupsLeavesComponent {

  @Input() parentGroup: GroupContent = null;
  @Input() groups: GroupContent[];
  @Input() leaves: LeafContent[];

  isContentShowed: boolean = false;

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
    this.groups.push(newGroup);
  }

  onNewLeafCreated(newLeafWithId: NewLeafWithId) {
    const newLeaf: LeafContent = {
      id: newLeafWithId.id,
      name: newLeafWithId.name,
      vid: newLeafWithId.vid,
      valueType: newLeafWithId.valueType,
      value: newLeafWithId.value,
    }
    this.leaves.push(newLeaf);
  }

  switchShowed() {
    this.isContentShowed = !this.isContentShowed;
  }

  onUpdateLeaf(updatedLeaf: UpdatedLeaf) {
    const leafContent = this.leaves.find(leaf => leaf.id === updatedLeaf.id);

    leafContent.name = updatedLeaf.name;
    leafContent.valueType = updatedLeaf.valueType;
    leafContent.value = updatedLeaf.value;
  }

  onGroupUpdate(updatedGroup: UpdatedGroup) {
    const groupContent = this.groups.find(group => group.id === updatedGroup.id);

    groupContent.name = updatedGroup.name;
  }
}
