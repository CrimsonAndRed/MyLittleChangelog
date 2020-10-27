import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Http } from 'app/http/http.service';
import { EditGroupModalComponent } from './edit-group-modal/edit-group-modal.component';
import { GroupContent, GroupToUpdate, Group } from 'app/model/group-content';

@Component({
  selector: 'edit-group-button',
  templateUrl: './edit-group-button.component.html',
  styleUrls: ['./edit-group-button.component.scss']
})
export class EditGroupButtonComponent {

  @Input() group: GroupContent;
  @Input() parentGroupId: number;
  @Output() onUpdateGroup = new EventEmitter<Group>();

  constructor(private http: Http, private route: ActivatedRoute, private dialog: MatDialog) {
  }

  onEditClick(): void {
    const dialogRef = this.dialog.open(EditGroupModalComponent, {
      hasBackdrop: true,
      data: this.group
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateGroup(result);
      }
    });
  }

  updateGroup(group: GroupContent): void {
    const versionId = this.route.snapshot.data.version.id;
    const groupId = this.group.id;

    const groupToUpdate: GroupToUpdate = {
      name: group.name,
      parentId: this.parentGroupId
    };
    this.http.put<Group>(`http://localhost:8080/version/${versionId}/group/${groupId}`, groupToUpdate)
      .subscribe(updatedGroup => this.onUpdateGroup.emit(updatedGroup));
  }

}
