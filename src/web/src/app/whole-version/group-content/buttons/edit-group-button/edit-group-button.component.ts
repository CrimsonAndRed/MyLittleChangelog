import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Http } from 'app/http/http.service';
import { EditGroupModalComponent } from './edit-group-modal/edit-group-modal.component';
import { GroupContent, GroupToUpdate, Group } from 'app/model/group-content';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/whole-version/whole-version.service';

@Component({
  selector: 'edit-group-button',
  templateUrl: './edit-group-button.component.html',
  styleUrls: ['./edit-group-button.component.scss']
})
export class EditGroupButtonComponent {

  @Input() group: GroupContent;
  @Input() parentGroupVid: number;
  @Output() onUpdateGroup = new EventEmitter<Observable<void>>();

  constructor(private http: Http,
              private route: ActivatedRoute,
              private dialog: MatDialog,
              private wholeVersionService: WholeVersionService) {
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
    const versionId = this.wholeVersionService.wholeVersion.id;
    const groupId = this.group.id;

    const groupToUpdate: GroupToUpdate = {
      name: group.name,
      parentVid: this.parentGroupVid
    };
    this.onUpdateGroup.emit(this.http.put<void>(`http://localhost:8080/version/${versionId}/group/${groupId}`, groupToUpdate));
  }

}
