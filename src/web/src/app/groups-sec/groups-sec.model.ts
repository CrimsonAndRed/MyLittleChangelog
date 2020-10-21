import { EventEmitter } from '@angular/core';
import { GroupContent } from 'app/model/group-content';

export interface GroupHeader {
  group: GroupContent;
  onGroupChange: EventEmitter<GroupContent>;
}
