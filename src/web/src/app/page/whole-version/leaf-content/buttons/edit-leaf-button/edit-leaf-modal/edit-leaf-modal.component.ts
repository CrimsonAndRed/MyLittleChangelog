import { Component, Input, Inject } from '@angular/core';

import { LeafContent } from 'app/model/leaf-content';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupContent } from 'app/model/group-content';
import { LeafTypeService } from 'app/service/leaf-type.service';
import { LeafType } from 'app/model/leaf-content';
import { Subject } from 'rxjs';
import { TreeNode } from 'app/model/tree';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';

@Component({
  selector: 'edit-leaf-modal',
  templateUrl: './edit-leaf-modal.component.html',
  styleUrls: ['./edit-leaf-modal.component.scss']
})
export class EditLeafModalComponent {

  leaf: LeafContent;
  _node: TreeNode<GroupContent>;
  newParentGroupVid: number;
  leafTypes: LeafType[] = null;
  _expandMap: Map<number, boolean>

  parentChangeSubject: Subject<number> = new Subject();

  constructor(@Inject(MAT_DIALOG_DATA) private data: EditLeafModalData,
              public leafTypeService: LeafTypeService,
              public wholeVersionService: WholeVersionService) {
    this.leaf = { ...this.data.leaf };
    this._node = this.data.node;
    this.leafTypes = [...leafTypeService.leafTypes()];

    this.newParentGroupVid = this._node.value?.vid;
    this.parentChangeSubject.subscribe(parent => this.newParentGroupVid = parent);
    this._expandMap = new Map(wholeVersionService.expandMap);
    // TODO special case for root?
    this._expandMap.set(undefined, true);
  }
}

export interface EditLeafModalData {
  leaf: LeafContent;
  node: TreeNode<GroupContent>;
}
