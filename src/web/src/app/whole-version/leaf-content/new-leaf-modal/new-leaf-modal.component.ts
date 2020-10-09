import { Component, Input, Output } from '@angular/core';

import { LeafContent, NewLeafWithId, NewLeaf } from 'app/model/leaf-content';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'new-leaf-modal',
  templateUrl: './new-leaf-modal.component.html',
  styleUrls: ['./new-leaf-modal.component.scss']
})
export class NewLeafModalComponent {

  leaf: NewLeaf = {
    name: null,
    valueType: null,
    value: null
  };

  constructor(private dialogRef: MatDialogRef<NewLeafModalComponent>) {
  }
}
