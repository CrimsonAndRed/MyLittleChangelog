import { Component, Output, EventEmitter, Input } from '@angular/core';
import { GlobalHeader, GlobalHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupContent, Group } from 'app/model/group-content';
import { WholeVersionService } from 'app/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'global-movement-header',
  templateUrl: './global-header.component.html',
  styleUrls: ['./global-header.component.scss']
})
export class GroupMovementGlobalHeaderComponent implements GlobalHeader {

  data: GlobalHeaderData;
  ctx: GroupsSecContext;

  constructor() { }

  onNodeCheckRadioClick(): void {
    this.ctx.emitGroupChoice(null);
  }

  isRadioButtonShowed(): boolean {
    return !this.ctx.forbiddenVids.has(null);
  }
}
