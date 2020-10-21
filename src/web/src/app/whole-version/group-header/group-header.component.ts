import { Component, Input, Output, EventEmitter } from '@angular/core';
import { GroupContent, NewGroupWithId } from 'app/model/group-content';
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

}
