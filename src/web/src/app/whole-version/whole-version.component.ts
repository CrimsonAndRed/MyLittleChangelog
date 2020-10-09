import { Component, OnInit } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router'
import { Http } from 'app/http/http.service'
import { GroupContent, NewGroupWithId } from 'app/model/group-content';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  version: WholeVersion;

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  onNewGroupCreated(newGroupWithId: NewGroupWithId): void {
    const newGroup: GroupContent = {
      id: newGroupWithId.id,
      name: newGroupWithId.name,
      vid: newGroupWithId.vid,
      groupContent: [],
      leafContent: []
    };

    this.version.groupContent.push(newGroup);
  }

  ngOnInit() {
    this.version = this.route.snapshot.data.version;
  }
}
