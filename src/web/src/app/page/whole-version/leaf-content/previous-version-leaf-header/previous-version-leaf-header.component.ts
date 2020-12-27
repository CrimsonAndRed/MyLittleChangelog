import { Component } from '@angular/core';
import { GroupsSecContext, LeafHeader, LeafHeaderData } from 'app/groups-sec/groups-sec.model';

@Component({
  selector: 'previous-version-leaf-header',
  templateUrl: './previous-version-leaf-header.component.html',
  styleUrls: ['./previous-version-leaf-header.component.scss']
})
export class PreviousVersionLeafHeaderComponent implements LeafHeader {

  data: LeafHeaderData;
  ctx: GroupsSecContext;

  constructor() { }

}
