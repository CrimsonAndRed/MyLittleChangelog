import { Component, Inject, OnInit, Type } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupsSecConfig, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { Http } from 'app/http/http.service';
import { GroupContent } from 'app/model/group-content';
import { WholeVersion } from 'app/model/whole-version';
import { FromPastGroupsModal } from 'app/whole-version/past-groups/from-past-groups-modal/from-past-groups-modal.component';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { GroupHeaderComponent } from '../header/group-header/group-header.component';
import { LeafHeaderComponent } from '../header/leaf-header/leaf-header.component';
import { groupContentToPrevious, PastGroupContent, PastLeafContent, PastRadioEvent, PreviousUsedGroupsAndLeaves } from '../previous-version.model';

@Component({
  selector: 'previous-version-modal',
  templateUrl: './previous-version-modal.component.html',
  styleUrls: ['./previous-version-modal.component.scss']
})
export class PreviousVersionModalComponent implements OnInit {

  config: GroupsSecConfig = {
    groupHeader: GroupHeaderComponent,
    leafHeader: LeafHeaderComponent,
  };

  context: GroupsSecContext;

  usedIds: PreviousUsedGroupsAndLeaves;
  groups: PastGroupContent[];
  chosenPastElement: PastRadioEvent = null;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: PreviousVersionModalData
  ) { }

  ngOnInit(): void {
    this.data.version
      .pipe(
        tap(v => this.usedIds = this.calculateUsedIds(v.groupContent)),
        tap(v => this.groups = v.groupContent.map(g => groupContentToPrevious(g, this.usedIds)))
      )
      .subscribe(() => this.context = {
        emitGroupCheck: this.onPastGroupCheck.bind(this),
        emitLeafCheck: this.onPastLeafCheck.bind(this),
        usedIds: this.usedIds
      });
  }

  onPastGroupCheck(group: PastGroupContent, groupId: number): void {
    this.chosenPastElement = {
      value: group,
      parentId: groupId,
      kind: 'group',
    };
  }

  onPastLeafCheck(leaf: PastLeafContent, groupId: number): void {
    this.chosenPastElement = {
      value: leaf,
      parentId: groupId,
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

export interface PreviousVersionModalData {
  version: Observable<WholeVersion>;
}