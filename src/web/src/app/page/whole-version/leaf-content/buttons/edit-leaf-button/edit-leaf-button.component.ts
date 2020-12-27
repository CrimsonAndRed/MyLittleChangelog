import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Http } from 'app/service/http.service';
import { EditLeafModalComponent } from './edit-leaf-modal/edit-leaf-modal.component';
import { LeafContent, LeafToUpdate, UpdatedLeaf } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'edit-leaf-button',
  templateUrl: './edit-leaf-button.component.html',
  styleUrls: ['./edit-leaf-button.component.scss']
})
export class EditLeafButtonComponent {

  @Input() leaf: LeafContent;
  @Input() node: TreeNode<GroupContent>;
  @Output() onUpdateLeaf = new EventEmitter<Observable<void>>();

  constructor(private http: Http,
              private dialog: MatDialog,
              private wholeVersionService: WholeVersionService) {
  }

  onEditButtonClick(): void {
    const dialogRef = this.dialog.open(EditLeafModalComponent, {
      hasBackdrop: true,
      minWidth: '80%',
      data: {
        leaf: this.leaf,
        parentGroupVid: this.node.value.vid,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateLeaf(result[0], result[1]);
      }
    });
  }

  updateLeaf(leaf: LeafContent, newParentGroupVid: number): void {
    const versionId = this.wholeVersionService.wholeVersion.id;
    const parentId = this.node.value.id;
    const leafId = leaf.id;

    const leafToUpdate: LeafToUpdate = {
      name: leaf.name,
      valueType: leaf.valueType,
      value: leaf.value,
      parentVid: newParentGroupVid,
    };
    this.onUpdateLeaf.emit(
      this.http.put<void>(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf/${leafId}`, leafToUpdate)
    );
  }
}
