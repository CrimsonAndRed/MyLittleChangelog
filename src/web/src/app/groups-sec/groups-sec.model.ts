import { Type } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';

export interface GlobalHeader extends Header<GlobalHeaderData> { }
export interface GroupHeader extends Header<GroupHeaderData> { }
export interface LeafHeader extends Header<LeafHeaderData> { }

export interface GlobalHeaderData {
  groups: GroupContent[];
}

export interface GroupHeaderData {
  group: GroupContent;
  parentGroup: GroupContent;
}

export interface LeafHeaderData {
  leaf: LeafContent;
  parentGroup: GroupContent;
}

export interface GroupsSecConfig {
  globalHeader: Type<GlobalHeader>;
  groupHeader: Type<GroupHeader>;
  leafHeader: Type<LeafHeader>;
  leafShowCondition: () => boolean;
  expandMap: Map<number, boolean>;
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
      globalHeader: null,
      groupHeader: null,
      leafHeader: null,
      leafShowCondition: () => true,
      expandMap: new Map()
    };
  }

  setGlobalHeader(header: Type<GlobalHeader>): GroupSecConfigBuilder {
    this.config.globalHeader = header;
    return this;
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

  setExpandMap(map: Map<number, boolean>): GroupSecConfigBuilder {
    this.config.expandMap = map;
    return this;
  }

  build(): GroupsSecConfig {
    return this.config;
  }
}
