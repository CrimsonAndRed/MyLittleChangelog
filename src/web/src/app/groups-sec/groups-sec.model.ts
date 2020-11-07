import { EventEmitter, Type } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { GroupLeavesSecComponent } from './group-leaves-sec/group-leaves-sec.component';

export interface GroupHeader extends Header<GroupHeaderData> { }
export interface LeafHeader extends Header<LeafHeaderData> { }

export interface GroupHeaderData {
  group: GroupContent;
  parentGroup: GroupContent;
}

export interface LeafHeaderData {
  leaf: LeafContent;
  parentGroup: GroupContent;
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
