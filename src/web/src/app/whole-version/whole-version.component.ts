import { Component, OnInit, Type } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';
import { GroupContent, Group } from 'app/model/group-content';
import { GroupHeader, GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext, LeafHeader } from 'app/groups-sec/groups-sec.model';
import { GroupHeaderComponent } from './group-content/group-header/group-header.component';
import { LeafHeaderComponent } from './leaf-content/leaf-header/leaf-header.component';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  version: WholeVersion;
  config: GroupsSecConfig = new GroupSecConfigBuilder()
    .setGroupHeader(GroupHeaderComponent)
    .setLeafHeader(LeafHeaderComponent)
    .build();
  context: GroupsSecContext = {
    allGroups: null
  };

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  handleNewGroup(group: GroupContent): void {
    this.version.groupContent.push(group);
  }

  ngOnInit(): void {
    const version = this.route.snapshot.data.version;
    this.context.allGroups = version.groupContent;
    this.version = version;
  }

  refresh(): void {
    const versionId = this.route.snapshot.params.id;

    this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
      .pipe(
        tap(v => this.context.allGroups = v.groupContent)
      )
      .subscribe(result => this.version = result);
  }
}
