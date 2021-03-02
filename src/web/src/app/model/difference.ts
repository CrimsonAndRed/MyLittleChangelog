import { Version } from "./version";

export interface Difference {
  from: Version;
  to: Version;
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
