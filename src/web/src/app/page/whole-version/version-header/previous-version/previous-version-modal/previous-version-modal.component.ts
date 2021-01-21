import { Component, Inject, OnInit, Input, Type } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupContent } from 'app/model/group-content';
import { tap } from 'rxjs/operators';
import {
  groupContentToPrevious,
  PastGroupContent,
  PastLeafContent,
  PastRadioEvent,
  PreviousUsedGroupsAndLeaves,
  PreviousVersionModalData,
} from 'app/model/previous-version';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';
import { Subject } from 'rxjs';
import { formatTree } from 'app/service/tree.service';

@Component({
  selector: 'previous-version-modal',
  templateUrl: './previous-version-modal.component.html',
  styleUrls: ['./previous-version-modal.component.scss']
})
export class PreviousVersionModalComponent implements OnInit {

  previousVersionRoot: TreeNode<PastGroupContent>;
  chosenPastElement: PastRadioEvent = null;

  nodeChooseSubject: Subject<PastRadioEvent> = new Subject();
  _expandMap: Map<number, boolean>;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: PreviousVersionModalData,
    private wholeVersionService: WholeVersionService
  ) { }

  ngOnInit(): void {
    const usedVids = this.calculateUsedVids();

    const rootChildren = this.data.version.groupContent.map(it => groupContentToPrevious(it, usedVids))

    const root: TreeNode<PastGroupContent> = formatTree(rootChildren, (g) => g.groupContent)
    this.previousVersionRoot = root;

    this.nodeChooseSubject.subscribe(e => {
      this.chosenPastElement = e;
    });

    this._expandMap = new Map(this.wholeVersionService.expandMap);
  }

  private calculateUsedVids(): PreviousUsedGroupsAndLeaves {
    const groupVids: Set<number> = new Set();
    const leafVids: Set<number> = new Set();

    this.wholeVersionService.wholeVersionTree.children.forEach(g => this.addUsedIdsRecursive(g, groupVids, leafVids));

    return {
      usedGroups: groupVids,
      usedLeaves: leafVids
    };
  }

  private addUsedIdsRecursive(group: TreeNode<GroupContent>, groupVids: Set<number>, leafVids: Set<number>): void {
    groupVids.add(group.value.vid);
    group.value.leafContent.forEach(l => leafVids.add(l.vid));
    group.children.forEach(g => this.addUsedIdsRecursive(g, groupVids, leafVids));
  }
}
