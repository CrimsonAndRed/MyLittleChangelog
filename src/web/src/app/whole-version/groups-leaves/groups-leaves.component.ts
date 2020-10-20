import { Component, Input } from '@angular/core';

import { GroupContent, NewGroupWithId, UpdatedGroup } from 'app/model/group-content';
import { LeafContent, NewLeafWithId, UpdatedLeaf } from 'app/model/leaf-content';

@Component({
  selector: 'groups-leaves',
  templateUrl: './groups-leaves.component.html',
  styleUrls: ['./groups-leaves.component.scss']
})
export class GroupsLeavesComponent {

  @Input() parentGroup: GroupContent = null;
  @Input() groups: GroupContent[];
  @Input() leaves: LeafContent[];
  @Input() canChange: boolean;

  @Input() isContentShowed: boolean = false;

  constructor() {
  }

  onNewGroupCreated(newGroupWithId: NewGroupWithId) {
    const newGroup: GroupContent = {
      id: newGroupWithId.id,
      name: newGroupWithId.name,
      vid: newGroupWithId.vid,
      realNode: true,
      groupContent: [],
      leafContent: [],
      isContentShowed: false,
    };
    this.groups.push(newGroup);
    this.isContentShowed = true;
  }

  onGroupUpdate(updatedGroup: UpdatedGroup) {
    const groupContent = this.groups.find(group => group.id === updatedGroup.id);

    groupContent.name = updatedGroup.name;
  }

  onMakeGroupReal(updatedGroup: NewGroupWithId) {
    const groupContent = this.groups.find(group => group.vid === updatedGroup.vid);

    groupContent.id = updatedGroup.id;
    groupContent.name = updatedGroup.name;
    groupContent.realNode = true;
  }

  onGroupDelete(deletedGroup: GroupContent) {
    this.groups = this.groups.filter(group => group.id !== deletedGroup.id);
  }

  onNewLeafCreated(newLeafWithId: NewLeafWithId) {
    const newLeaf: LeafContent = {
      id: newLeafWithId.id,
      name: newLeafWithId.name,
      vid: newLeafWithId.vid,
      valueType: newLeafWithId.valueType,
      value: newLeafWithId.value,
      groupVid: newLeafWithId.groupVid
    }
    this.leaves.push(newLeaf);
    this.isContentShowed = true;
  }

  switchShowed() {
    if (this.isContentShowed) {
      this.switchShowedDeeply();
    } else {
      this.isContentShowed = !this.isContentShowed;
    }
  }

  switchShowedDeeply() {
    this.isContentShowed = !this.isContentShowed;
    for (let group of this.groups) {
      this.updateContentForGroupRecursive(group);
    }
  }

  updateContentForGroupRecursive(groupContent: GroupContent) {
    groupContent.isContentShowed = this.isContentShowed;
    for (let group of groupContent.groupContent) {
      this.updateContentForGroupRecursive(group);
    }
  }

  onLeafUpdate(updatedLeaf: UpdatedLeaf) {
    const leafContent = this.leaves.find(leaf => leaf.id === updatedLeaf.id);

    leafContent.name = updatedLeaf.name;
    leafContent.valueType = updatedLeaf.valueType;
    leafContent.value = updatedLeaf.value;
  }

  onLeafDelete(deletedLeaf: LeafContent) {
    this.leaves = this.leaves.filter(leaf => leaf.id !== deletedLeaf.id);
  }
}
