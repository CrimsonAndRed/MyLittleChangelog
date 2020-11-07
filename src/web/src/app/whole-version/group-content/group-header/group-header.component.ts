import { Component, EventEmitter } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, NewLeafWithId } from 'app/model/leaf-content';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { WholeVersionService } from 'app/service/whole-version.service';

@Component({
  selector: 'group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class GroupHeaderComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  constructor(private wholeVersionService: WholeVersionService) { }

  handleNewGroup(newGroupWithId: Group): void {
    const newGroup: GroupContent = {
      id: newGroupWithId.id,
      name: newGroupWithId.name,
      vid: newGroupWithId.vid,
      realNode: true,
      isEarliest: true,
      groupContent: [],
      leafContent: []
    };

    this.wholeVersionService.addGroupToParent(newGroup, this.data.group.vid);
  }

  handleNewLeaf(newLeafWithId: NewLeafWithId): void {
    const newLeaf: LeafContent = {
      id: newLeafWithId.id,
      name: newLeafWithId.name,
      vid: newLeafWithId.vid,
      valueType: newLeafWithId.valueType,
      value: newLeafWithId.value,
      groupVid: newLeafWithId.groupVid
    };
    this.wholeVersionService.addLeafToParent(newLeaf, this.data.group.vid)
  }

  handleDeleteGroup(): void {
    this.wholeVersionService.deleteGroup(this.data.group.id, this.data.parentGroup?.vid)
  }

  handleUpdateGroup(group: Group): void {
    this.wholeVersionService.updateGroup(group);
  }

  handleMaterializeGroup(group: Group): void {
    this.wholeVersionService.materializeGroup(group);
  }

  handleDematerializeGroup(group: Group): void {
    this.wholeVersionService.dematerializeGroup(group);
  }
}
