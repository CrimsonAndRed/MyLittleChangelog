export interface LeafContent {
  id: number;
  vid: number;
  name: string;
  valueType: number;
  value: string;
}

export interface PastLeafContent {
  id: number;
  vid: number;
  name: string;
  valueType: number;
  value: string;
  inCurrentVersion: boolean;
}

export interface NewLeaf {
    name: string,
    valueType: number,
    value: string,
    vid: number,
}

export interface NewLeafWithId extends NewLeaf {
    id: number,
}

export interface LeafToUpdate {
  name: string,
  valueType: number,
  value: string,
  parentId: number
}

export interface UpdatedLeaf {
  id: number,
  vid: number,
  name: string,
  valueType: number,
  value: string,
}
