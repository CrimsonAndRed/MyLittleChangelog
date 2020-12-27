import { Component, Inject, OnInit, Input, Type } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupContent } from 'app/model/group-content';
import { tap } from 'rxjs/operators';
import { GroupHeaderComponent } from '../header/group-header/group-header.component';
import { LeafHeaderComponent } from '../header/leaf-header/leaf-header.component';
import {
  groupContentToPrevious,
  PastGroupContent,
  PastLeafContent,
  PastRadioEvent,
  PreviousUsedGroupsAndLeaves,
  PreviousVersionModalData,
} from 'app/model/previous-version';

@Component({
  selector: 'previous-version-modal',
  templateUrl: './previous-version-modal.component.html',
  styleUrls: ['./previous-version-modal.component.scss']
})
export class PreviousVersionModalComponent implements OnInit {

  config: GroupsSecConfig = new GroupSecConfigBuilder()
    .setGroupHeader(GroupHeaderComponent)
    .setLeafHeader(LeafHeaderComponent)
    .build();

  context: GroupsSecContext;

  usedIds: PreviousUsedGroupsAndLeaves;
  groups: PastGroupContent[];
  chosenPastElement: PastRadioEvent = null;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: PreviousVersionModalData
  ) { }

  ngOnInit(): void {
    this.usedIds = this.calculateUsedIds(this.data.currentGroups);
    this.groups = this.data.version.groupContent.map(g => groupContentToPrevious(g, this.usedIds));
    this.context = {
      emitGroupCheck: this.onPastGroupCheck.bind(this),
      emitLeafCheck: this.onPastLeafCheck.bind(this),
      usedIds: this.usedIds
    };
  }

  onPastGroupCheck(group: PastGroupContent, parentGroup: GroupContent): void {
    this.chosenPastElement = {
      value: group,
      parentId: parentGroup?.id,
      parentVid: parentGroup?.vid,
      kind: 'group',
    };
  }

  onPastLeafCheck(leaf: PastLeafContent, parentGroup: GroupContent): void {
    this.chosenPastElement = {
      value: leaf,
      parentId: parentGroup?.id,
      parentVid: parentGroup?.vid,
      kind: 'leaf',
    };
  }

  private calculateUsedIds(groups: GroupContent[]): PreviousUsedGroupsAndLeaves {
    const groupIds: Set<number> = new Set();
    const leafIds: Set<number> = new Set();

    groups.forEach(g => this.addUsedIdsRecursive(g, groupIds, leafIds));

    return {
      usedGroups: groupIds,
      usedLeaves: leafIds
    };
  }

  private addUsedIdsRecursive(group: GroupContent, groupIds: Set<number>, leafIds: Set<number>): void {
    groupIds.add(group.id);
    group.leafContent.forEach(l => leafIds.add(l.id));
    group.groupContent.forEach(g => this.addUsedIdsRecursive(g, groupIds, leafIds));
  }
}
