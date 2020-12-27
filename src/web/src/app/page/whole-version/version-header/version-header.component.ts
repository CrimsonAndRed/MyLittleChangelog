import { Component, Output, EventEmitter, Input } from '@angular/core';
import { GlobalHeader, GlobalHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupContent, Group } from 'app/model/group-content';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'version-header',
  templateUrl: './version-header.component.html',
  styleUrls: ['./version-header.component.scss']
})
export class VersionHeaderComponent implements GlobalHeader {

  data: GlobalHeaderData;
  ctx: GroupsSecContext;

  constructor(private wholeVersionService: WholeVersionService, private preloaderService: PreloaderService) {}

  handleNewGroup(obs: Observable<Group>): void {
    this.preloaderService.wrap(
      obs.pipe(
        tap(newGroupWithId => {
          const newGroup: GroupContent = {
            id: newGroupWithId.id,
            name: newGroupWithId.name,
            vid: newGroupWithId.vid,
            realNode: true,
            isEarliest: true,
            groupContent: [],
            leafContent: []
          };

          this.wholeVersionService.addGroupToParent(newGroup, null);
        })
      )
    );
  }

  onPreviousNodeChosen(obs: Observable<void>): void {
    this.ctx.previousNodeChosen(obs);
  }
}
