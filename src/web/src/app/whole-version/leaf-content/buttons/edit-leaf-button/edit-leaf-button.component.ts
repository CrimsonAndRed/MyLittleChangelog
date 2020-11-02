import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Http } from 'app/http/http.service';
import { EditLeafModalComponent } from './edit-leaf-modal/edit-leaf-modal.component';
import { LeafContent, LeafToUpdate, UpdatedLeaf } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'edit-leaf-button',
  templateUrl: './edit-leaf-button.component.html',
  styleUrls: ['./edit-leaf-button.component.scss']
})
export class EditLeafButtonComponent {

  @Input() leaf: LeafContent;
<<<<<<< HEAD
  @Input() parentGroup: GroupContent;
=======
  @Input() parentGroupId: number;
  @Input() allGroups: GroupContent[];
>>>>>>> 77d09b9... еализация переноса лифа из группы в группу
  @Output() onUpdateLeaf = new EventEmitter<UpdatedLeaf>();

  constructor(private http: Http, private route: ActivatedRoute, private dialog: MatDialog) {
  }

  onEditButtonClick(): void {
    const dialogRef = this.dialog.open(EditLeafModalComponent, {
      hasBackdrop: true,
      minWidth: '80%',
      data: {
        leaf: this.leaf,
        parentGroupId: this.parentGroupId,
        allGroups: this.allGroups,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateLeaf(result);
      }
    });

  }

  updateLeaf(leaf: LeafContent): void {
    const versionId = this.route.snapshot.data.version.id;
    const parentId = this.parentGroup.id;
    const leafId = leaf.id;

    const leafToUpdate: LeafToUpdate = {
      name: leaf.name,
      valueType: leaf.valueType,
      value: leaf.value,
<<<<<<< HEAD
      parentVid: this.parentGroup?.vid,
=======
      // TODO Испрвить на vid
      parentVid: leaf.groupVid,
>>>>>>> 77d09b9... еализация переноса лифа из группы в группу
    };
    this.http.put<UpdatedLeaf>(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf/${leafId}`, leafToUpdate)
          .subscribe(updatedLeaf => this.onUpdateLeaf.emit(updatedLeaf));
  }
}
