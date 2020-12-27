import { Component, Inject } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'edit-group-modal',
  templateUrl: './edit-group-modal.component.html',
  styleUrls: ['./edit-group-modal.component.scss']
})
export class EditGroupModalComponent {

  _node: TreeNode<GroupContent>;
  _group: GroupContent;
  parentGroupVid: number;
  newParentGroupVid: number;

  constructor(private dialogRef: MatDialogRef<EditGroupModalComponent>, @Inject(MAT_DIALOG_DATA) private data: EditGroupModalData) {
    this._node = data.node;
    this._group = { ...this._node.value };
    this.parentGroupVid = this._node.parent?.value?.vid;
    this.newParentGroupVid = this._node.parent?.value?.vid;
  }

  onParentChange(groupVid: number): void {
    this.newParentGroupVid = groupVid;
  }
}

export interface EditGroupModalData {
  node: TreeNode<GroupContent>;
}
