import { GroupContent } from './group-content';

export interface WholeVersionHeader {
  id: number;
  canChange: boolean;
}
export interface WholeVersion extends WholeVersionHeader {
  groupContent: GroupContent[];
}