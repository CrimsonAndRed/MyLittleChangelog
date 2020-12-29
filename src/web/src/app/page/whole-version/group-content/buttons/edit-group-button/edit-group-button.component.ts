import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Http } from 'app/service/http.service';
import { EditGroupModalComponent } from './edit-group-modal/edit-group-modal.component';
import { GroupContent, GroupToUpdate, Group } from 'app/model/group-content';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'edit-group-button',
  templateUrl: './edit-group-button.component.html',
  styleUrls: ['./edit-group-button.component.scss']
})
export class EditGroupButtonComponent {

  @Input() node: TreeNode<GroupContent>;
  @Output() onUpdateGroup = new EventEmitter<Observable<void>>();

  constructor(private http: Http,
              private dialog: MatDialog,
              private wholeVersionService: WholeVersionService) {
  }

  onEditClick(): void {
    const dialogRef = this.dialog.open(EditGroupModalComponent, {
      hasBackdrop: true,
      minWidth: '80%',
      data: {
        node: this.node,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateGroup(result);
      }
    });
  }

  updateGroup(data: EditGroupModalReturned): void {
    const { group, newParentVid } = data;
    const versionId = this.wholeVersionService.wholeVersionHeader.id;
    const groupId = this.node.value.id;

    const groupToUpdate: GroupToUpdate = {
      name: group.name,
      parentVid: newParentVid
    };
    this.onUpdateGroup.emit(this.http.put<void>(`http://localhost:8080/version/${versionId}/group/${groupId}`, groupToUpdate));
  }

}

export interface EditGroupModalData {
  node: TreeNode<GroupContent>;
}

export interface EditGroupModalReturned {
  group: GroupContent;
  newParentVid: number;
}