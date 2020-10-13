import { Component, Input, Output } from '@angular/core';

import { WholeVersion } from 'app/model/whole-version';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { PastRadioEvent } from 'app/model/past-radio-event'

@Component({
  selector: 'from-past-groups-modal',
  templateUrl: './from-past-groups-modal.component.html',
  styleUrls: ['./from-past-groups-modal.component.scss']
})
export class FromPastGroupsModal {

  version: WholeVersion = {
    id: null,
    groupContent: [],
  };

  chosenPastElement: PastRadioEvent = null

  constructor(private dialogRef: MatDialogRef<FromPastGroupsModal>, private http: Http) {
    this.http.get<WholeVersion>("http://localhost:8080/version/previous")
      .subscribe((wholeVersion) => this.version = wholeVersion);
  }

  onRadioChange(event: PastRadioEvent) {
    this.chosenPastElement = event
  }
}
