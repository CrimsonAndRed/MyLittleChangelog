import { Component, Input } from '@angular/core';
import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'group-content-sec',
  templateUrl: './group-content-sec.component.html',
  styleUrls: ['./group-content-sec.component.scss']
})
export class GroupContentSecComponent {
  @Input() group: GroupContent;
  constructor() { }
}
