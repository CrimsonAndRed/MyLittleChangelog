import { Component, Input } from '@angular/core';
import { GroupDifference } from 'app/model/difference';

@Component({
  selector: 'groups-leaves-third',
  templateUrl: './groups-leaves-third.component.html',
  styleUrls: ['./groups-leaves-third.component.scss']
})
export class GroupsLeavesThirdComponent {

  @Input() group: GroupDifference;

  constructor() { }
}
