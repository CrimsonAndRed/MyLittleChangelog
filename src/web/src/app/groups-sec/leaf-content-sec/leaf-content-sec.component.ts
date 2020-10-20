import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
// import { MatDialog } from '@angular/material/dialog';
// import { Http } from 'app/http/http.service'
//
import { LeafContent, UpdatedLeaf, LeafToUpdate } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'leaf-content-sec',
  templateUrl: './leaf-content-sec.component.html',
  styleUrls: ['./leaf-content-sec.component.scss']
})
export class LeafContentSecComponent {

  @Output() onLeafUpdate = new EventEmitter<UpdatedLeaf>();
  @Output() onLeafDelete = new EventEmitter<LeafContent>();

  @Input() leafContent: LeafContent;
  @Input() parentId: number = null;
  @Input() canChange: boolean = false;

  constructor(
//              private http: Http,
              private route: ActivatedRoute,
//              private dialog: MatDialog
              ) {
  }

//  onEditButtonClick() {
//    const dialogRef = this.dialog.open(EditLeafModalComponent, {
//      hasBackdrop: true,
//      data: {
//        leaf: this.leafContent,
//        groups: this.groups
//      }
//    });
//
//    dialogRef.afterClosed().subscribe(result => {
//      if (result) {
//        this.updateLeaf(result);
//      }
//    });
//
//  }
//
//  onDeleteButtonClick() {
//    const versionId = this.route.snapshot.data.version.id;
//    const groupId = this.parentId;
//    const leafId = this.leafContent.id;
//
//    this.http.delete(`http://localhost:8080/version/${versionId}/group/${groupId}/leaf/${leafId}`)
//      .subscribe(() => this.onLeafDelete.emit(this.leafContent));
//  }
//
//  updateLeaf(leaf: LeafContent) {
//    const versionId = this.route.snapshot.data.version.id;
//    const parentId = this.parentId;
//    const leafId = leaf.id;
//
//    const leafToUpdate: LeafToUpdate = {
//      name: leaf.name,
//      valueType: leaf.valueType,
//      value: leaf.value,
//      parentId: parentId,
//    }
//    this.http.put<UpdatedLeaf>(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf/${leafId}`, leafToUpdate)
//          .subscribe(updatedLeaf => this.onLeafUpdate.emit(updatedLeaf));
//  }

}
