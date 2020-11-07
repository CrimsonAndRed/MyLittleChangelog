export interface LeafContent {
  id: number;
  vid: number;
  name: string;
  valueType: number;
  value: string;
  // TODO(#8) что это?
  groupVid: number;
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
