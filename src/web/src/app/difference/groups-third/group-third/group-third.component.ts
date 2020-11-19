import { Component, Input } from '@angular/core';
import { GroupDifference } from 'app/model/difference';

@Component({
  selector: 'group-third',
  templateUrl: './group-third.component.html',
  styleUrls: ['./group-third.component.scss']
})
export class GroupThirdComponent {
  @Input() group: GroupDifference;
  constructor() { }
}
