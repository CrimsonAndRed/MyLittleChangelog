import { Component, Input } from '@angular/core';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';
import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'whole-version-node',
  templateUrl: './whole-version-node.component.html',
  styleUrls: ['./whole-version-node.component.scss']
})
export class WholeVersionNodeComponent {

  @Input() node: TreeNode<GroupContent>;

  constructor(public wholeVersionService: WholeVersionService) { }
}
