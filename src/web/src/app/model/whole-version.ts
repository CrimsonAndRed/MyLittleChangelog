import { GroupContent, PastGroupContent } from './group-content';

export interface WholeVersion {
  id: number;
  canChange: boolean;
  groupContent: GroupContent[];
}

export interface PreviousVersionsContent {
  groupContent: PastGroupContent[];
}
