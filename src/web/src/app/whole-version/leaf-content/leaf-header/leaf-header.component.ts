import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LeafHeader, GroupChangeFn, LeafHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupContent } from 'app/model/group-content';
import { LeafContent, UpdatedLeaf } from 'app/model/leaf-content';

@Component({
  selector: 'leaf-header',
  templateUrl: './leaf-header.component.html',
  styleUrls: ['./leaf-header.component.scss']
})
export class LeafHeaderComponent implements LeafHeader {

  data: LeafHeaderData;
  ctx: GroupsSecContext;

  constructor() {}

  handleDeleteLeaf(): void {
    let fn = (groupId) => (gl, t) => {
      let newArray = gl.filter(g => g.id !== groupId);
      if (t !== null) {
        if (newArray.length == 0 && (t.leaves === null || t.leaves.length == 0) && !t.group.realNode) {
          t.onParentChange.emit(fn(t.group.id))
        }
      }
      return newArray;
    }

    this.data.parentChange.emit((g, t) => {
      g.leafContent = g.leafContent.filter(l => l.id !== this.data.leaf.id);
      if ((g.groupContent === null || g.groupContent.length == 0) && (g.leafContent === null || g.leafContent.length == 0) && !g.realNode) {
        t.onParentChange.emit(fn(g.id))
      }
      return g;
    });
  }

  handleUpdateLeaf(updatedLeaf: UpdatedLeaf): void {
    const newLeafContent: LeafContent = {
      id: updatedLeaf.id,
      vid: updatedLeaf.vid,
      name: updatedLeaf.name,
      valueType: updatedLeaf.valueType,
      value: updatedLeaf.value,
      groupVid: updatedLeaf.groupVid,
    };
    this.data.leafChange.emit(newLeafContent);
    if (newLeafContent.groupVid !== this.data.leaf.groupVid) {
      this.handleGroupMovement(newLeafContent);
    }
  }

  private handleGroupMovement(newLeafContent: LeafContent): void {
    const newGroup = this.findGroupByVid(newLeafContent.groupVid, this.ctx.allGroups);
    newGroup.leafContent.push(newLeafContent);
    this.data.parentChange.emit((g) => {
      g.leafContent = g.leafContent.filter(l => l.vid !== this.data.leaf.vid);
      return g;
    });
  }

  private findGroupByVid(vid: number, groups: GroupContent[]): GroupContent {
    return groups.find(g => g.vid === vid ? g : this.findGroupByVid(vid, g.groupContent));
  }

}
