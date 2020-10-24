import { Component, Input, OnInit, Type } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';

import {
  GroupsSecConfig,
  GroupsSecContext,
  ParentGroupListChangeFn,
} from './groups-sec.model';

@Component({
  selector: 'groups-sec',
  templateUrl: './groups-sec.component.html',
  styleUrls: ['./groups-sec.component.scss']
})
export class GroupsSecComponent implements OnInit {

  @Input() groups: GroupContent[];
  @Input() config: GroupsSecConfig;
  @Input() context: GroupsSecContext;

  constructor() { }

  ngOnInit() {
  }

  onGroupListChange(fn: ParentGroupListChangeFn) {
    this.groups = fn(this.groups);
  }

}
