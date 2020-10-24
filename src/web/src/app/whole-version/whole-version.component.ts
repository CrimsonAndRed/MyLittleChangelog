import { Component, OnInit, Type } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router'
import { Http } from 'app/http/http.service'
import { GroupContent, NewGroupWithId } from 'app/model/group-content';
import { GroupHeader, GroupsSecConfig, LeafHeader } from 'app/groups-sec/groups-sec.model';

import { GroupHeaderComponent } from './group-header/group-header.component';
import { LeafHeaderComponent } from './leaf-header/leaf-header.component';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  version: WholeVersion;
  config: GroupsSecConfig = {
    groupHeader: GroupHeaderComponent,
    leafHeader: LeafHeaderComponent
  };

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  handleNewGroup(group: GroupContent) {
    this.version.groupContent.push(group);
  }

  ngOnInit() {
    this.version = this.route.snapshot.data.version;
  }

  refresh(): void {
    const versionId = this.route.snapshot.params.id;

    this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
      .subscribe(result => this.version = result);
  }
}
