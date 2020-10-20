import { Component, Input, Inject } from '@angular/core';

import { LeafContent, NewLeafWithId, NewLeaf } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'edit-leaf-modal',
  templateUrl: './edit-leaf-modal.component.html',
  styleUrls: ['./edit-leaf-modal.component.scss']
})
export class EditLeafModalComponent {

  _leaf: LeafContent;
  _groups: GroupContent[];

  constructor(private dialogRef: MatDialogRef<EditLeafModalComponent>, @Inject(MAT_DIALOG_DATA) private data: LeafGroups) {
    this._leaf = { ...data.leaf };
    this._groups = { ...data.groups };
  }
}

interface LeafGroups {
  leaf: LeafContent;
  groups: GroupContent[];
}
