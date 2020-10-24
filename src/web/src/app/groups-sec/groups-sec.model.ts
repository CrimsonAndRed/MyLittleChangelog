import { EventEmitter, Type } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';

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
  groupId: number;
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

export type ParentGroupListChangeFn = (list: GroupContent[]) => GroupContent[];
export type GroupChangeFn = (group: GroupContent) => GroupContent;

export interface GroupsSecContext {
  [payload: string]: any;
}
