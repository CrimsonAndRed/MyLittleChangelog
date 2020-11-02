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

export interface GroupsSecConfig {
  groupHeader: Type<GroupHeader>;
  leafHeader: Type<LeafHeader>;
  leafShowCondition: () => boolean;
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

//
// BUILDERS
//

export class GroupSecConfigBuilder {
  private readonly config: GroupsSecConfig;

  constructor() {
    this.config = {
      groupHeader: null,
      leafHeader: null,
      leafShowCondition: () => true,
    };
  }

  setGroupHeader(header: Type<GroupHeader>): GroupSecConfigBuilder {
    this.config.groupHeader = header;
    return this;
  }

  setLeafHeader(header: Type<LeafHeader>): GroupSecConfigBuilder {
    this.config.leafHeader = header;
    return this;
  }

  setLeafShowCondition(condition: () => boolean): GroupSecConfigBuilder {
    this.config.leafShowCondition = condition;
    return this;
  }

  build(): GroupsSecConfig {
    return this.config;
  }
}
