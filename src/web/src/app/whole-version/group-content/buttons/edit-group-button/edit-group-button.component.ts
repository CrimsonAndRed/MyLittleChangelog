import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Http } from 'app/http/http.service';
import { EditGroupModalComponent } from './edit-group-modal/edit-group-modal.component';
import { GroupContent, GroupToUpdate, Group } from 'app/model/group-content';
import { Observable } from 'rxjs';

@Component({
  selector: 'edit-group-button',
  templateUrl: './edit-group-button.component.html',
  styleUrls: ['./edit-group-button.component.scss']
})
export class EditGroupButtonComponent {

  @Input() group: GroupContent;
  @Input() parentGroupVid: number;
  @Output() onUpdateGroup = new EventEmitter<Observable<Group>>();

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
    const versionId = this.route.snapshot.params.id;
    const groupId = this.group.id;

    const groupToUpdate: GroupToUpdate = {
      name: group.name,
      parentVid: this.parentGroupVid
    };
    this.onUpdateGroup.emit(this.http.put<Group>(`http://localhost:8080/version/${versionId}/group/${groupId}`, groupToUpdate));
  }

}
