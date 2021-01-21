import { Component, EventEmitter, Input } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'group-body',
  templateUrl: './group-body.component.html',
  styleUrls: ['./group-body.component.scss']
})
export class GroupBodyComponent {

  @Input() node: TreeNode<GroupContent>

  constructor() { }
}
