import { Component, Input, Output, EventEmitter } from '@angular/core';
import { GroupContent, NewGroupWithId } from 'app/model/group-content';
import { LeafContent, NewLeafWithId, UpdatedLeaf } from 'app/model/leaf-content';
import { GroupHeader } from 'app/groups-sec/groups-sec.model';

@Component({
  selector: 'group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class GroupHeaderComponent implements GroupHeader {

  onGroupChange = new EventEmitter<GroupContent>();
  group: GroupContent = null;

  constructor() { };

  handleNewGroup(newGroupWithId: NewGroupWithId) {
    const newGroup: GroupContent = {
            id: newGroupWithId.id,
            name: newGroupWithId.name,
            vid: newGroupWithId.vid,
            realNode: true,
            groupContent: [],
            leafContent: []
          };
    this.group.groupContent.push(newGroup);
    this.onGroupChange.emit(this.group);
  }

  handleNewLeaf(newLeafWithId: NewLeafWithId) {
    const newLeaf: LeafContent = {
      id: newLeafWithId.id,
      name: newLeafWithId.name,
      vid: newLeafWithId.vid,
      valueType: newLeafWithId.valueType,
      value: newLeafWithId.value,
      groupVid: newLeafWithId.groupVid
    }
    this.group.leafContent.push(newLeaf);
    this.onGroupChange.emit(this.group);
  }

}
