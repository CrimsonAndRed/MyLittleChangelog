import { Component } from '@angular/core';
import { GlobalHeader, GlobalHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';

@Component({
  selector: 'previous-version-version-header',
  templateUrl: './previous-version-version-header.component.html',
  styleUrls: ['./previous-version-version-header.component.scss']
})
export class PreviousVersionVersionHeaderComponent implements GlobalHeader {
  data: GlobalHeaderData;
  ctx: GroupsSecContext;

  constructor() { }
}
