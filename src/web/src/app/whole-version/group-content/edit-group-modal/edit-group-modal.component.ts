import { Component, Input, Inject } from '@angular/core';

import { GroupContent } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { NewGroupWithId, NewGroup } from 'app/model/new-group';
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

  constructor(private dialogRef: MatDialogRef<EditGroupModalComponent>, @Inject(MAT_DIALOG_DATA) private group: GroupContent) {
    this._group = { ...group };
  }
}
