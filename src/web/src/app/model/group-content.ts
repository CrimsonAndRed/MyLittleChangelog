import { LeafContent } from './leaf-content';

export interface GroupContent {
  id: number;
  vid: number;
  name: string;
  realNode: boolean;
  isEarliest: boolean;
  groupContent: GroupContent[];
  leafContent: LeafContent[];
}

export interface NewGroup {
    name: string;
    vid: number;
    parentVid: number;
}

export interface GroupToUpdate {
  name: string;
  parentVid: number;
}

export interface Group {
  id: number;
  vid: number;
  name: string;
  parentVid: number;
}
