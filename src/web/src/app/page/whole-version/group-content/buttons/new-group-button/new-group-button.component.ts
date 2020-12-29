import { Component, Input, Output } from '@angular/core';

import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { Http } from 'app/service/http.service';
import { EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewGroupModalComponent } from './new-group-modal/new-group-modal.component';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'new-group-button',
  templateUrl: './new-group-button.component.html',
  styleUrls: ['./new-group-button.component.scss']
})
export class NewGroupButtonComponent {

  @Output() onNewGroup = new EventEmitter<Observable<Group>>();

  @Input() node: TreeNode<GroupContent>;

  constructor(private http: Http,
              private dialog: MatDialog,
              private wholeVersionService: WholeVersionService) {
  }

  onNewGroupButtonClick(): void {
    const dialogRef = this.dialog.open(NewGroupModalComponent, {
      hasBackdrop: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        this.createNewGroup(result);
      }
    });
  }


  createNewGroup(name: string) {
    const versionId = this.wholeVersionService.wholeVersionHeader.id;
    const parentVid = this.node.value?.vid;

    const newGroup: NewGroup = {
      vid: null,
      parentVid,
      name,
    };
    this.onNewGroup.emit(this.http.post<Group>(`http://localhost:8080/version/${versionId}/group`, newGroup));
  }
}
