import { Component, Input, Output } from '@angular/core';

import { GroupContent } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { NewLeaf, NewLeafWithId } from 'app/model/leaf-content';
import { NewGroup, NewGroupWithId } from 'app/model/group-content';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { FromPastGroupsModal } from '../from-past-groups-modal/from-past-groups-modal.component';

@Component({
  selector: 'from-past-groups-button',
  templateUrl: './from-past-groups-button.component.html',
  styleUrls: ['./from-past-groups-button.component.scss']
})
export class FromPastGroupsButton {

  @Output() onRefresh = new EventEmitter<void>();

  @Input() groupContent: GroupContent;

  constructor(private http: Http, private route: ActivatedRoute, private dialog: MatDialog) {
  }

  onAddGroupsFromPastClick() {

    const dialogRef = this.dialog.open(FromPastGroupsModal, {
      hasBackdrop: true,
      minWidth: "80%"
    });

    dialogRef.afterClosed().subscribe(result => {
      switch(result?.kind) {
        case "leaf":
          this.addLeafFromPast({
            vid: result.value.vid,
            name: result.value.name,
            value: result.value.value,
            valueType: result.value.valueType
          }, result.parentId);
          break;
        case "group":
          this.addGroupFromPast({
            vid: result.value.vid,
            name: result.value.name,
            parentId: result.parentId
          });
          break;
        default:
          break;
      }
    });
  }

  addGroupFromPast(newGroup: NewGroup) {
    const versionId = this.route.snapshot.data.version.id;

    this.http.post<NewGroupWithId>(`http://localhost:8080/version/${versionId}/group`, newGroup)
          .subscribe(newGroup => this.onRefresh.emit());
  }

  addLeafFromPast(newLeaf: NewLeaf, parentId: number) {
    const versionId = this.route.snapshot.data.version.id;

    this.http.post<NewLeafWithId>(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf`, newLeaf)
          .subscribe(newLeaf => this.onRefresh.emit());
  }
}
