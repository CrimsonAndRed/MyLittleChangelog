import { Component, Output, EventEmitter, Input } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { WholeVersionService } from 'app/service/whole-version.service'

@Component({
  selector: 'version-header',
  templateUrl: './version-header.component.html',
  styleUrls: ['./version-header.component.scss']
})
export class VersionHeaderComponent {

  @Input() groups: GroupContent[];

  @Output() previousNodeChosen = new EventEmitter<void>();

  constructor(private wholeVersionService: WholeVersionService) {}

  handleNewGroup(newGroupWithId: Group): void {
    const newGroup: GroupContent = {
      id: newGroupWithId.id,
      name: newGroupWithId.name,
      vid: newGroupWithId.vid,
      realNode: true,
      isEarliest: true,
      groupContent: [],
      leafContent: []
    };

    this.wholeVersionService.addGroupToParent(newGroup, null);
  }

  onPreviousNodeChosen(): void {
    this.previousNodeChosen.emit();
  }
}
