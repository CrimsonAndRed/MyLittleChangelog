import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Http } from 'app/http/http.service'

import { LeafContent } from 'app/model/leaf-content';
import { UpdatedLeaf, LeafToUpdate } from 'app/model/update-leaf';
import { EditLeafModalComponent } from './edit-leaf-modal/edit-leaf-modal.component'

@Component({
  selector: 'leaf-content',
  templateUrl: './leaf-content.component.html',
  styleUrls: ['./leaf-content.component.scss']
})
export class LeafContentComponent {

  @Output() onUpdateLeaf = new EventEmitter<UpdatedLeaf>();

  @Input() leafContent: LeafContent;

  @Input() parentId: number


  constructor(private http: Http, private route: ActivatedRoute, private dialog: MatDialog) {
  }

  onEditLeafButtonClick() {
    const dialogRef = this.dialog.open(EditLeafModalComponent, {
      hasBackdrop: true,
      data: this.leafContent
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateLeaf(result);
      }
    });

  }

  updateLeaf(leaf: LeafContent) {
    const versionId = this.route.snapshot.data.version.id;
    const parentId = this.parentId;
    const leafId = leaf.id;

    const leafToUpdate: LeafToUpdate = {
      name: leaf.name,
      valueType: leaf.valueType,
      value: leaf.value,
      parentId: parentId,
    }
    this.http.put<UpdatedLeaf>(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf/${leafId}`, leafToUpdate)
          .subscribe(updatedLeaf => this.onUpdateLeaf.emit(updatedLeaf));
  }
}
