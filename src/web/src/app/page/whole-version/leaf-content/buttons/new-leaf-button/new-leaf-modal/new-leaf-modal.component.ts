import { Component } from '@angular/core';

import { NewLeaf } from 'app/model/leaf-content';
import { MatDialogRef } from '@angular/material/dialog';
import { LeafType } from 'app/model/leaf-content';
import { LeafTypeService} from 'app/service/leaf-type.service';

@Component({
  selector: 'new-leaf-modal',
  templateUrl: './new-leaf-modal.component.html',
  styleUrls: ['./new-leaf-modal.component.scss']
})
export class NewLeafModalComponent {

  leaf: NewLeaf = {
    name: null,
    valueType: null,
    value: null,
    vid: null
  };

  leafTypes: LeafType[] = null;

  constructor(private dialogRef: MatDialogRef<NewLeafModalComponent>, public leafTypeService: LeafTypeService) {
    this.leafTypes = [...leafTypeService.leafTypes()];
  }

  onLeafTypeChanged(leafType) {
    this.leaf.value = null;
  }
}
