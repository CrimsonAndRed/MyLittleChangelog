import { Component, Output, EventEmitter } from '@angular/core';
import { GroupContent, NewGroupWithId } from 'app/model/group-content';

@Component({
  selector: 'version-header',
  templateUrl: './version-header.component.html',
  styleUrls: ['./version-header.component.scss']
})
export class VersionHeaderComponent {

  @Output() onNewGroup = new EventEmitter<GroupContent>();

  handleNewGroup(newGroupWithId: NewGroupWithId) {
      const newGroup: GroupContent = {
        id: newGroupWithId.id,
        name: newGroupWithId.name,
        vid: newGroupWithId.vid,
        realNode: true,
        groupContent: [],
        leafContent: []
      };
      this.onNewGroup.emit(newGroup);
    }

}
