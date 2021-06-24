import { GroupContent } from './group-content';

export interface WholeVersionHeader {
  id: number;
  canChange: boolean;
  name: string;
}
export interface WholeVersion extends WholeVersionHeader {
  groupContent: GroupContent[];
}