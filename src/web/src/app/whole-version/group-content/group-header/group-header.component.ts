import { Component, EventEmitter } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, NewLeafWithId } from 'app/model/leaf-content';
import { GroupHeader, GroupHeaderData, GroupsSecContext, ParentGroupListChangeFn } from 'app/groups-sec/groups-sec.model';

@Component({
  selector: 'group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class GroupHeaderComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  constructor() { }

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
    this.data.group.groupContent.push(newGroup);
    this.data.groupChange.emit(this.data.group);
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
    this.data.group.leafContent.push(newLeaf);
    this.data.groupChange.emit(this.data.group);
  }

  handleDeleteGroup(): void {
    let fn = (groupId) => ((gl, t) => {
      let newArray = gl.filter(g => g.id !== groupId);
      if (t !== null) {
        if (newArray.length == 0 && (t.leaves == null || t.leaves.length == 0) && !t.group.realNode) {
          t.onParentChange.emit(fn(t.group.id))
        }
      }
      return newArray;
    });

    this.data.parentGroupsChange.emit(fn(this.data.group.id));
  }

  handleUpdateGroup(group: Group): void {
    this.data.group.name = group.name;
    this.data.groupChange.emit(this.data.group);
  }

  handleMaterializeGroup(group: Group): void {
    this.data.group.name = group.name;
    this.data.group.vid = group.vid;
    this.data.group.id = group.id;
    this.data.group.realNode = true;
    this.data.group.isEarliest = false;

    this.data.groupChange.emit(this.data.group);
  }

  handleDematerializeGroup(group: Group): void {
    this.data.group.name = group.name;
    this.data.group.vid = group.vid;
    this.data.group.id = group.id;
    this.data.group.realNode = false;

    this.data.groupChange.emit(this.data.group);

    let fn = (groupId) => ((gl, t) => {
      if (  (t === null && !gl.find(gr => gr.id === groupId).realNode) ||
            (t !== null && t.group.groupContent.length === 1 && (t.group.groupContent[0].leafContent === null || t.group.groupContent[0].leafContent.length === 0) && (t.leaves == null || t.leaves.length == 0))) {
        let newArray = gl.filter(g => g.id !== groupId);
        if (t) {
          t.onParentChange.emit(fn(t.group.id))
        }
        return newArray;
      }

      return gl;
    });

    this.data.parentGroupsChange.emit(fn(group.id));
  }
}
