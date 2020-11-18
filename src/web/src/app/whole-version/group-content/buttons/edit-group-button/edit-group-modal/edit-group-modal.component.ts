import { Component, Input, Inject } from '@angular/core';

import { GroupContent, NewGroup } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'edit-group-modal',
  templateUrl: './edit-group-modal.component.html',
  styleUrls: ['./edit-group-modal.component.scss']
})
export class EditGroupModalComponent {

  _group: GroupContent;
  parentGroupVid: number;
  newParentGroupVid: number;

  constructor(private dialogRef: MatDialogRef<EditGroupModalComponent>, @Inject(MAT_DIALOG_DATA) private data: EditGroupModalData) {
    this._group = { ...data.group };
    this.parentGroupVid = data.parentGroupVid;
    this.newParentGroupVid = data.parentGroupVid;
  }

  onParentChange(groupVid: number): void {
    this.newParentGroupVid = groupVid;
  }
}

export interface EditGroupModalData {
  group: GroupContent;
  parentGroupVid: number;
}
