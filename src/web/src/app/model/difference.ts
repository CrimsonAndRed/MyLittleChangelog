export interface Difference {
  from: number;
  to: number;
  groupContent: GroupDifference[];
  leafContent: LeafDifference[];
}

export interface GroupDifference {
  id: number;
  vid: number;
  name: string;
  groupContent: GroupDifference[];
  leafContent: LeafDifference[];
}

export interface LeafDifference {
  id: number;
  vid: number;
  name: string;
  valueType: number;
  valueDiff: string;
}
