import { LeafContent } from './leaf-content';

export interface GroupContent {
  id: number;
  vid: number;
  name: string;
  groupContent: GroupContent[];
  leafContent: LeafContent[];
}
