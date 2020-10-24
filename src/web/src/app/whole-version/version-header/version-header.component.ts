import { Component, Output, EventEmitter, Input } from '@angular/core';
import { GroupContent, NewGroupWithId } from 'app/model/group-content';

@Component({
  selector: 'version-header',
  templateUrl: './version-header.component.html',
  styleUrls: ['./version-header.component.scss']
})
export class VersionHeaderComponent {

  @Input() groups: GroupContent[];

  @Output() onNewGroup = new EventEmitter<GroupContent>();
  @Output() previousNodeChosen = new EventEmitter<void>();

  handleNewGroup(newGroupWithId: NewGroupWithId): void {
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

  onPreviousNodeChosen(): void {
    this.previousNodeChosen.emit();
  }

}
