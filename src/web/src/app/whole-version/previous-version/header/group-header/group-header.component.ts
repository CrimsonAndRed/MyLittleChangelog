import { Component } from '@angular/core';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { groupContentToPrevious } from '../../previous-version.model';

@Component({
  selector: 'group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class GroupHeaderComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  constructor() { }

  onNodeChecked(): void {
    this.ctx.emitGroupCheck(groupContentToPrevious(this.data.group, this.ctx.usedIds), this.data.parentGroup);
  }

  showRadioButton(): boolean {
    return !this.ctx.usedIds.usedGroups.has(this.data.group.id)
  }
}