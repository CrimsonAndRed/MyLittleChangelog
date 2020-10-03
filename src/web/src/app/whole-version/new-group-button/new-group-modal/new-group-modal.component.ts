import { Component, Input, Output } from '@angular/core';

import { GroupContent } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { NewGroupWithId, NewGroup} from 'app/model/new-group';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'new-group-modal',
  templateUrl: './new-group-modal.component.html',
  styleUrls: ['./new-group-modal.component.scss']
})
export class NewGroupModalComponent {

  name: string = '';

  constructor(private dialogRef: MatDialogRef<NewGroupModalComponent>) {
  }
}
