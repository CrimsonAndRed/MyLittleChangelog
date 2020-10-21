import { EventEmitter } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';

export interface GroupHeader {
  group: GroupContent;
  parentGroup: GroupContent;
  onParentGroupsChange: EventEmitter<ParentGroupListChangeFn>;
  onGroupChange: EventEmitter<GroupContent>;
}

export interface ParentGroupListChangeFn {
  (list: GroupContent[]): GroupContent[];
}

export interface LeafHeader {
  leaf: LeafContent;
  groupId: number;
  onLeafChange: EventEmitter<LeafContent>;
  onParentChange: EventEmitter<GroupChangeFn>;
}

export interface GroupChangeFn {
  (group: GroupContent): GroupContent;
}
