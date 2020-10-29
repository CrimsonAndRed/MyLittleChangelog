import { EventEmitter, Type } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { GroupLeavesSecComponent } from './group-leaves-sec/group-leaves-sec.component';

export interface GroupHeader extends Header<GroupHeaderData> { }
export interface LeafHeader extends Header<LeafHeaderData> { }

export interface GroupHeaderData {
  group: GroupContent;
  parentGroup: GroupContent;
  parentGroupsChange: EventEmitter<ParentGroupListChangeFn>;
  groupChange: EventEmitter<GroupContent>;
}

export interface LeafHeaderData {
  leaf: LeafContent;
  parentGroup: GroupContent;
  leafChange: EventEmitter<LeafContent>;
  parentChange: EventEmitter<GroupChangeFn>;
}

export class GroupsSecConfig {
  groupHeader: Type<GroupHeader> = null;
  leafHeader: Type<LeafHeader> = null;
}

export interface Header<T> {
  data: T;
  ctx: GroupsSecContext;
}

// TODO#5 это плохая структура
export type ParentGroupListChangeFn = (list: GroupContent[], t: GroupLeavesSecComponent) => GroupContent[];
export type GroupChangeFn = (group: GroupContent, t: GroupLeavesSecComponent) => GroupContent;

export interface GroupsSecContext {
  [payload: string]: any;
}
