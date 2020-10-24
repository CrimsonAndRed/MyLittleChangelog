import { Component, Input, Inject } from '@angular/core';

import { LeafContent } from 'app/model/leaf-content';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'edit-leaf-modal',
  templateUrl: './edit-leaf-modal.component.html',
  styleUrls: ['./edit-leaf-modal.component.scss']
})
export class EditLeafModalComponent {

  _leaf: LeafContent;

  constructor(private dialogRef: MatDialogRef<EditLeafModalComponent>, @Inject(MAT_DIALOG_DATA) private data: LeafContent) {
    this._leaf = { ...data };
  }
}
