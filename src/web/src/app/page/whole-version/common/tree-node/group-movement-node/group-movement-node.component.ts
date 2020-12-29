import { Component, EventEmitter, Input, Output } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';
import { Subject } from 'rxjs';

@Component({
  selector: 'group-movement-node',
  templateUrl: './group-movement-node.component.html',
  styleUrls: ['./group-movement-node.component.scss']
})
export class GroupMovementNodeComponent {

  @Input() node: TreeNode<GroupContent>;
  @Input() original: TreeNode<GroupContent>;
  @Input() parentChangeSubject: Subject<number>;
  @Input() expandMap: Map<number, boolean>;

  constructor(public wholeVersionService: WholeVersionService) { }

  onNodeCheckRadioClick() {
    this.parentChangeSubject.next(this.node.value?.vid)
  }

  changeGlobalContentShow(value: boolean): void {
    this.setGlobalExpandValue(this.node, value);
  }

  changeLocalContentShow(value: boolean): void {
    this.setLocalExpandValue(this.node, value);
  }

  setLocalExpandValue(node: TreeNode<GroupContent>, value: boolean) {
    this.expandMap.set(node.value?.vid, value);
  }

  setGlobalExpandValue(node: TreeNode<GroupContent>, value: boolean) {
    this.expandMap.set(node.value?.vid, value);
    node.children.forEach(c => this.setGlobalExpandValue(c, value))
  }

  isContentShowed(): boolean {
    return this.expandMap.get(this.node.value?.vid) === true;
  }

  isExpandButtonShowed(): boolean {
    return !(this.node.value && this.node.value.groupContent.length == 0);
  }
}
