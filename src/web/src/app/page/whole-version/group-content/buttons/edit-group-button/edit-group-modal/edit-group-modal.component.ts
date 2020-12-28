import { Component, Inject } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TreeNode } from 'app/model/tree';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'edit-group-modal',
  templateUrl: './edit-group-modal.component.html',
  styleUrls: ['./edit-group-modal.component.scss']
})
export class EditGroupModalComponent {

  _node: TreeNode<GroupContent>;
  _group: GroupContent;
  newParentGroupVid: number;
  parentChangeSubject: Subject<number> = new Subject();

  constructor(private dialogRef: MatDialogRef<EditGroupModalComponent>,
              @Inject(MAT_DIALOG_DATA) private data: EditGroupModalData,
              public wholeVersionService: WholeVersionService) {
    this._node = data.node;
    this._group = { ...data.node.value };
    this.newParentGroupVid = data.node.parent?.value?.vid;
    this.parentChangeSubject.subscribe(parent => this.newParentGroupVid = parent);
  }
}

export interface EditGroupModalData {
  node: TreeNode<GroupContent>;
}
