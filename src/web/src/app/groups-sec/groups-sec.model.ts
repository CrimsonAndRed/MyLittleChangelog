import { EventEmitter } from '@angular/core';
import { GroupContent } from 'app/model/group-content';

export interface GroupHeader {
  group: GroupContent;
  parentGroup: GroupContent;
  onParentGroupsChange: EventEmitter<ParentGroupListChangeFn>;
  onGroupChange: EventEmitter<GroupContent>;
}

export interface ParentGroupListChangeFn {
  (list: GroupContent[]): GroupContent[];
}
