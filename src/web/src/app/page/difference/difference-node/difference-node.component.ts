import { Component, EventEmitter, Input, Output } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'difference-node',
  templateUrl: './difference-node.component.html',
  styleUrls: ['./difference-node.component.scss']
})
export class DifferenceNodeComponent {

  @Input() node: TreeNode<GroupContent>;

  constructor() { }
}
