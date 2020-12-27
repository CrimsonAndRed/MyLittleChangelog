import { Component, Input } from '@angular/core';
import { GroupDifference } from 'app/model/difference';

@Component({
  selector: 'groups-third',
  templateUrl: './groups-third.component.html',
  styleUrls: ['./groups-third.component.scss']
})
export class GroupsThirdComponent {

  @Input() groups: GroupDifference[];

  constructor() { }
}
