import { Component, Input } from '@angular/core';
import { TreeNode } from 'app/model/tree';
import { PastGroupContent, PastLeafContent, PastRadioEvent } from 'app/model/previous-version';
import { Subject } from 'rxjs';

@Component({
  selector: 'previous-version-node',
  templateUrl: './previous-version-node.component.html',
  styleUrls: ['./previous-version-node.component.scss']
})
export class PreviousVersionNodeComponent {

  @Input() node: TreeNode<PastGroupContent>;
  @Input() expandMap: Map<number, boolean>;
  @Input() nodeCheckSubject: Subject<PastRadioEvent>;

  constructor() { }

  showGroupInput(): boolean {
    return !this.node.value.inCurrentVersion;
  }

  showLeafInput(leaf: PastLeafContent): boolean {
    return !leaf.inCurrentVersion;
  }

  onGroupNodeCheckRadioClick() {
    this.nodeCheckSubject.next({
      value: this.node,
      parentId: this.node.parent.value?.id,
      kind: 'group',
    });
  }

  onLeafNodeCheckRadioClick(leaf: PastLeafContent) {
    this.nodeCheckSubject.next({
      value: leaf,
      parentId: this.node.value.id,
      kind: 'leaf',
    });
  }
}
