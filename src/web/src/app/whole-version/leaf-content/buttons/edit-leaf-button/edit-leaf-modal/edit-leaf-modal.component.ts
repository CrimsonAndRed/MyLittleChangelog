import { Component, Input, Inject } from '@angular/core';

import { LeafContent } from 'app/model/leaf-content';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupContent } from 'app/model/group-content';
import { LeafTypeService } from 'app/service/leaf-type.service';

@Component({
  selector: 'edit-leaf-modal',
  templateUrl: './edit-leaf-modal.component.html',
  styleUrls: ['./edit-leaf-modal.component.scss']
})
export class EditLeafModalComponent {

  leaf: LeafContent;
  parentGroupVid: number;
  newParentGroupVid: number;

  constructor(@Inject(MAT_DIALOG_DATA) private data: EditLeafModalData, public leafTypeService: LeafTypeService) {
    this.leaf = { ...this.data.leaf };
    // TODO(#5) неимплементированы другие значения
    this.leaf.valueType = 1;
    this.parentGroupVid = this.data.parentGroupVid;
    this.newParentGroupVid = this.data.parentGroupVid;
  }

  onParentChange(groupVid: number): void {
    this.newParentGroupVid = groupVid;
  }
}

export interface EditLeafModalData {
  leaf: LeafContent;
  parentGroupVid: number;
}
