import { PastGroupContent } from 'app/previous-version/previous-version.model';
import { GroupContent } from './group-content';

export interface WholeVersion {
  id: number;
  canChange: boolean;
  groupContent: GroupContent[];
}

export interface PreviousVersionsContent {
  groupContent: PastGroupContent[];
}
