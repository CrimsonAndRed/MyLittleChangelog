import { Component, Inject, OnInit, Input, Type } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
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

@Component({
  selector: 'previous-version-modal',
  templateUrl: './previous-version-modal.component.html',
  styleUrls: ['./previous-version-modal.component.scss']
})
export class PreviousVersionModalComponent implements OnInit {

  previousVersionRoot: TreeNode<PastGroupContent>;
  chosenPastElement: PastRadioEvent = null;

  nodeChooseSubject: Subject<PastRadioEvent> = new Subject();

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: PreviousVersionModalData,
    private wholeVersionService: WholeVersionService
  ) { }

  ngOnInit(): void {
    const usedIds = this.calculateUsedIds();

    const rootChildren = this.data.version.groupContent.map(it => groupContentToPrevious(it, usedIds))

    const root: TreeNode<PastGroupContent> = { parent: null, children: [], value: null };
    this.formatTreeNode(root, rootChildren)
    this.previousVersionRoot = root;

    this.nodeChooseSubject.subscribe(e => {
      this.chosenPastElement = e;
    });
  }


  private formatTreeNode(node: TreeNode<PastGroupContent>, groups: PastGroupContent[]) {
    node.children = groups.map((g) => {
      const newNode: TreeNode<PastGroupContent> = { parent: node, children: [], value: g }
      this.formatTreeNode(newNode, g.groupContent)
      return newNode;
    })
  }

  private calculateUsedIds(): PreviousUsedGroupsAndLeaves {
    const groupIds: Set<number> = new Set();
    const leafIds: Set<number> = new Set();

    this.wholeVersionService.wholeVersionTree.children.forEach(g => this.addUsedIdsRecursive(g, groupIds, leafIds));

    return {
      usedGroups: groupIds,
      usedLeaves: leafIds
    };
  }

  private addUsedIdsRecursive(group: TreeNode<GroupContent>, groupIds: Set<number>, leafIds: Set<number>): void {
    groupIds.add(group.value.id);
    group.value.leafContent.forEach(l => leafIds.add(l.id));
    group.children.forEach(g => this.addUsedIdsRecursive(g, groupIds, leafIds));
  }
}
