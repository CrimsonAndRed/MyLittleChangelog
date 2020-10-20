import { Component, Input, Output, Inject } from '@angular/core';

import { WholeVersion, PreviousVersionsContent } from 'app/model/whole-version';
import { PastLeafContent, LeafContent } from 'app/model/leaf-content';
import { PastGroupContent, GroupContent } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PastRadioEvent } from 'app/model/past-radio-event'

@Component({
  selector: 'from-past-groups-modal',
  templateUrl: './from-past-groups-modal.component.html',
  styleUrls: ['./from-past-groups-modal.component.scss']
})
export class FromPastGroupsModal {

  version: PreviousVersionsContent = {
    groupContent: [],
  };

  chosenPastElement: PastRadioEvent = null

  constructor(private dialogRef: MatDialogRef<FromPastGroupsModal>, private http: Http,  @Inject(MAT_DIALOG_DATA) private sets: [Set<number>, Set<number>]) {
    this.http.get<WholeVersion>("http://localhost:8080/version/previous")
      .subscribe((wholeVersion) => this.createPreviousVersions(wholeVersion));
  }

  createPreviousVersions(wholeVersion: WholeVersion) {
    let groupContent: PastGroupContent[] = [];

    for (let group of wholeVersion.groupContent) {
      groupContent.push(this.createPreviousGroup(group));
    }

    this.version = {
      groupContent: groupContent
    };
  }

  createPreviousGroup(group: GroupContent): PastGroupContent {
    let g: PastGroupContent = {
      id: group.id,
      vid: group.vid,
      name: group.name,
      realNode: group.realNode,
      inCurrentVersion: this.sets[0].has(group.id),
      groupContent: [],
      leafContent: [],
    }
    for (let groupContent of group.groupContent) {
      g.groupContent.push(this.createPreviousGroup(groupContent));
    }

    for (let leafContent of group.leafContent) {
      let leaf: PastLeafContent = {
        id: leafContent.id,
        vid: leafContent.vid,
        name: leafContent.name,
        valueType: leafContent.valueType,
        value: leafContent.value,
        inCurrentVersion: this.sets[1].has(leafContent.id),
        groupVid: leafContent.groupVid
      }

      g.leafContent.push(leaf);
    }
    return g;
  }

  onRadioChange(event: PastRadioEvent) {
    this.chosenPastElement = event
  }
}
