import { GroupContent } from './group-content';
import { LeafContent } from './leaf-content';

export interface WholeVersion {
  id: number;
  groupContent: GroupContent[];
  leafContent: LeafContent[];
}
