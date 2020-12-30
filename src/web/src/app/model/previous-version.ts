import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { TreeNode } from './tree';

export interface PastVersion {
  groupContent: GroupContent[]
}

export interface PastGroupContent {
  id: number;
  vid: number;
  name: string;
  realNode: boolean;
  isEarliest: boolean;
  inCurrentVersion: boolean;
  groupContent: PastGroupContent[];
  leafContent: PastLeafContent[];
}

export interface PastLeafContent {
  id: number;
  vid: number;
  name: string;
  valueType: number;
  value: string;
  inCurrentVersion: boolean;
}


export interface PastRadioEvent {
  value: TreeNode<PastGroupContent> | PastLeafContent;
  parentId: number | null;
  kind: 'leaf' | 'group';
}


export interface PreviousUsedGroupsAndLeaves {
  usedGroups: Set<number>;
  usedLeaves: Set<number>;
}

export interface PreviousVersionModalData {
  version: PastVersion;
}


export function groupContentToPrevious(group: GroupContent, usedVids: PreviousUsedGroupsAndLeaves): PastGroupContent {
  return {
    id: group.id,
    vid: group.vid,
    name: group.name,
    realNode: group.realNode,
    isEarliest: group.isEarliest,
    inCurrentVersion: usedVids.usedGroups.has(group.vid),
    groupContent: group.groupContent.map(g => groupContentToPrevious(g, usedVids)),
    leafContent: group.leafContent.map(l => leafContentToPrevious(l, usedVids))
  };
}

export function leafContentToPrevious(leaf: LeafContent, usedVids: PreviousUsedGroupsAndLeaves): PastLeafContent {
  return {
    id: leaf.id,
    vid: leaf.vid,
    name: leaf.name,
    valueType: leaf.valueType,
    value: leaf.value,
    inCurrentVersion: usedVids.usedLeaves.has(leaf.id),
  };
}
