import { PastGroupContent } from 'app/model/previous-version';
import { GroupContent } from './group-content';

export interface WholeVersion {
  id: number;
  canChange: boolean;
  groupContent: GroupContent[];
}

export interface PreviousVersionsContent {
  groupContent: PastGroupContent[];
}
