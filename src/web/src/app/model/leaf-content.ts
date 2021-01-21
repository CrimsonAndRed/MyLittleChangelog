export interface LeafContent {
  id: number;
  vid: number;
  name: string;
  valueType: number;
  value: string;
}

export interface NewLeaf {
  name: string;
  valueType: number;
  value: string;
  vid: number;
}

export interface NewLeafWithId extends NewLeaf {
  id: number;
  groupVid: number;
}

export interface LeafToUpdate {
  name: string;
  valueType: number;
  value: string;
  parentVid: number;
}

export interface UpdatedLeaf {
  id: number;
  vid: number;
  name: string;
  valueType: number;
  value: string;
  groupVid: number;
}

export interface LeafType {
  id: number;
  name: string;
}
