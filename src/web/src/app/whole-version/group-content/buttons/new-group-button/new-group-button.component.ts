import { Component, Input, Output } from '@angular/core';

import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewGroupModalComponent } from './new-group-modal/new-group-modal.component';

@Component({
  selector: 'new-group-button',
  templateUrl: './new-group-button.component.html',
  styleUrls: ['./new-group-button.component.scss']
})
export class NewGroupButtonComponent {

  @Output() onNewGroup = new EventEmitter<Group>();

  @Input() parentGroupId: number = null;

  constructor(private http: Http, private route: ActivatedRoute, private dialog: MatDialog) {
  }

  onNewGroupButtonClick(): void {
    const dialogRef = this.dialog.open(NewGroupModalComponent, {
      hasBackdrop: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.createNewGroup(result);
      }
    });
  }

  createNewGroup(name: string): void {
    const versionId = this.route.snapshot.data.version.id;
    const parentId = this.parentGroupId;

    const newGroup: NewGroup = {
      vid: null,
      parentId,
      name,
    };
    this.http.post<Group>(`http://localhost:8080/version/${versionId}/group`, newGroup)
      .subscribe(newGroup => this.onNewGroup.emit(newGroup) );
  }

}
