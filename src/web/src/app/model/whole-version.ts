import { GroupContent } from './group-content';

export interface WholeVersionHeader {
  id: number;
  canChange: boolean;
  name: string;
  projectId: number;
}
export interface WholeVersion extends WholeVersionHeader {
  groupContent: GroupContent[];
}