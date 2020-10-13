import { GroupContent, PastGroupContent } from './group-content';

export interface WholeVersion {
  id: number;
  groupContent: GroupContent[];
}

export interface PreviousVersionsContent {
  groupContent: PastGroupContent[];
}
