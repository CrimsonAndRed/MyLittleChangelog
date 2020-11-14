import { Component } from '@angular/core';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';

@Component({
  selector: 'previous-version-group-header',
  templateUrl: './previous-version-group-header.component.html',
  styleUrls: ['./previous-version-group-header.component.scss']
})
export class PreviousVersionGroupHeaderComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  constructor() { }

}
