import { Component } from '@angular/core';
import { NewGroup } from 'app/model/group-content';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';

@Component({
  selector: 'version-header',
  templateUrl: './version-header.component.html',
  styleUrls: ['./version-header.component.scss']
})
export class VersionHeaderComponent {

  userGroupIds

  constructor(public wholeVersionService: WholeVersionService) {}

  handleNewGroup(group: NewGroup): void {
    this.wholeVersionService.createNewGroup(group)
  }
}
