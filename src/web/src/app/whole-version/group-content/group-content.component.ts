import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

import { GroupContent, NewGroupWithId, GroupToUpdate, UpdatedGroup } from 'app/model/group-content';
import { LeafContent, NewLeafWithId } from 'app/model/leaf-content';
import { Http } from 'app/http/http.service';
import { EditGroupModalComponent } from './edit-group-modal/edit-group-modal.component'

@Component({
  selector: 'group-content',
  templateUrl: './group-content.component.html',
  styleUrls: ['./group-content.component.scss']
})
export class GroupContentComponent {

  @Output() onGroupUpdate = new EventEmitter<UpdatedGroup>();
  @Input() groupContent: GroupContent;
  @Input() parentId: number;

  constructor(private http: Http, private route: ActivatedRoute, private dialog: MatDialog) {
  }

  onEditLeafButtonClick() {
    const dialogRef = this.dialog.open(EditGroupModalComponent, {
          hasBackdrop: true,
          data: this.groupContent
        });

        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            this.updateGroup(result);
          }
        });
  }

  updateGroup(group: GroupContent) {
    const versionId = this.route.snapshot.data.version.id;
    const groupId = group.id;

    const groupToUpdate: GroupToUpdate = {
      name: group.name,
      parentId: this.parentId
    }
    this.http.put<UpdatedGroup>(`http://localhost:8080/version/${versionId}/group/${groupId}`, groupToUpdate)
      .subscribe(updatedGroup => this.onGroupUpdate.emit(updatedGroup))
  }

}
