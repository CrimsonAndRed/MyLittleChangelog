import { LeafContent } from './leaf-content';

export interface GroupContent {
  id: number;
  vid: number;
  name: string;
  realNode: boolean;
  groupContent: GroupContent[];
  leafContent: LeafContent[];
}

export interface NewGroup {
    name: string;
    vid: number;
    parentId: number;
}

export interface NewGroupWithId extends NewGroup {
    id: number;
}

export interface GroupToUpdate {
  name: string;
  parentId: number;
}

export interface UpdatedGroup {
  id: number;
  vid: number;
  name: string;
  parentId: number;
}
