import { Component, EventEmitter, Input, Output } from '@angular/core';
import { GroupDifference } from 'app/model/difference';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';
import { DifferenceService } from '../difference.service';

@Component({
  selector: 'difference-node',
  templateUrl: './difference-node.component.html',
  styleUrls: ['./difference-node.component.scss']
})
export class DifferenceNodeComponent {

  @Input() node: TreeNode<GroupDifference>;

  constructor(public differenceService: DifferenceService) { }

  changeGlobalContentShow(value: boolean): void {
    this.setGlobalExpandValue(this.node, value);
  }

  changeLocalContentShow(value: boolean): void {
    this.setLocalExpandValue(this.node, value);
  }

  setLocalExpandValue(node: TreeNode<GroupDifference>, value: boolean) {
    this.differenceService.expandMap.set(node.value.vid, value);
  }

  setGlobalExpandValue(node: TreeNode<GroupDifference>, value: boolean) {
    this.differenceService.expandMap.set(node.value.vid, value);
    node.children.forEach(c => this.setGlobalExpandValue(c, value))
  }

  isContentShowed(): boolean {
    return this.differenceService.expandMap.get(this.node.value.vid) === true;
  }

  isExpandButtonShowed(): boolean {
    return (this.node.value.groupContent.length !== 0) ||
      ((this.node.value.leafContent.length !== 0));
  }
}
