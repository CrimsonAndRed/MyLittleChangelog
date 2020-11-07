import { Component, Input, Inject } from '@angular/core';

import { LeafContent } from 'app/model/leaf-content';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'edit-leaf-modal',
  templateUrl: './edit-leaf-modal.component.html',
  styleUrls: ['./edit-leaf-modal.component.scss']
})
export class EditLeafModalComponent {

  leaf: LeafContent;
  allGroups: GroupContent[];
  parentGroupVid: number;

  constructor(@Inject(MAT_DIALOG_DATA) private data: EditLeafModalData) {
    this.leaf = { ...this.data.leaf };
    this.allGroups = [...this.data.allGroups];
    this.parentGroupVid = this.data.parentGroupVid;
  }

  onParentChange(groupVid: number): void {
    this.leaf.groupVid = groupVid;
  }
}

export interface EditLeafModalData {
  leaf: LeafContent;
  parentGroupVid: number;
  allGroups: GroupContent[];
}
