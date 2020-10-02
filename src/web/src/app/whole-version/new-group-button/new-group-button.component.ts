import { Component, Output } from '@angular/core';

import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'new-group-button',
  templateUrl: './new-group-button.component.html',
  styleUrls: ['./new-group-button.component.scss']
})
export class NewGroupButtonComponent {

  @Output() groupContent: GroupContent;

  constructor() {
  }

}
