import { PastGroupContent } from 'app/whole-version/previous-version/previous-version.model';
import { GroupContent } from './group-content';

export interface WholeVersion {
  id: number;
  canChange: boolean;
  groupContent: GroupContent[];
}

export interface PreviousVersionsContent {
  groupContent: PastGroupContent[];
}
