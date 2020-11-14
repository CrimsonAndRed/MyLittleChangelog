import { Component, Input, Output } from '@angular/core';

import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewGroupModalComponent } from './new-group-modal/new-group-modal.component';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/whole-version/whole-version.service';

@Component({
  selector: 'new-group-button',
  templateUrl: './new-group-button.component.html',
  styleUrls: ['./new-group-button.component.scss']
})
export class NewGroupButtonComponent {

  @Output() onNewGroup = new EventEmitter<Observable<Group>>();

  @Input() parentGroupVid: number = null;

  constructor(private http: Http,
              private route: ActivatedRoute,
              private dialog: MatDialog,
              private wholeVersionService: WholeVersionService) {
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


  createNewGroup(name: string) {
    const versionId = this.wholeVersionService.wholeVersion.id;
    const parentVid = this.parentGroupVid;

    const newGroup: NewGroup = {
      vid: null,
      parentVid,
      name,
    };
    this.onNewGroup.emit(this.http.post<Group>(`http://localhost:8080/version/${versionId}/group`, newGroup));
  }
}
