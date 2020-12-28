import { Component, Output, EventEmitter, Input, OnInit } from '@angular/core';
import { GlobalHeader, GlobalHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupContent, Group } from 'app/model/group-content';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { tap, switchMap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'version-header',
  templateUrl: './version-header.component.html',
  styleUrls: ['./version-header.component.scss']
})
export class VersionHeaderComponent implements OnInit {

  userGroupIds

  constructor(public wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService) {}

  ngOnInit(): void {

  }

  handleNewGroup(obs: Observable<Group>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(newGroupWithId => {
          const newGroup: GroupContent = {
            id: newGroupWithId.id,
            name: newGroupWithId.name,
            vid: newGroupWithId.vid,
            realNode: true,
            isEarliest: true,
            groupContent: [],
            leafContent: []
          };

          // this.wholeVersionService.addGroupToParent(newGroup, null);
          return this.wholeVersionService.createNewGroup();
        })
      )
    );
  }

  // TODO kindof strange
  onPreviousNodeChosen(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap((c) => this.wholeVersionService.createNewGroup())
      )
    );
  }
}
