import { Component, Input } from "@angular/core";
import { GroupContent } from "app/model/group-content";
import { TreeNode } from "app/model/tree";
import { WholeVersionService } from "../../whole-version.service";

@Component({
    selector: 'version-expand-block',
    templateUrl: './version-expand-block.component.html',
    styleUrls: ['./version-expand-block.component.scss']
  })
  export class VersionExpandBlockComponent {

  @Input() node: TreeNode<GroupContent>;
  @Input() expandMap: Map<number, boolean>;

  constructor(public wholeVersionService: WholeVersionService) {}


  changeGlobalContentShow(value: boolean): void {
    this.setGlobalExpandValue(this.node, value);
  }

  changeLocalContentShow(value: boolean): void {
    this.setLocalExpandValue(this.node, value);
  }

  setLocalExpandValue(node: TreeNode<GroupContent>, value: boolean) {
    let map = this.expandMap ? this.expandMap : this.wholeVersionService.expandMap;
    map.set(node.value?.vid, value);
  }

  setGlobalExpandValue(node: TreeNode<GroupContent>, value: boolean) {
    let map = this.expandMap ? this.expandMap : this.wholeVersionService.expandMap;
    map.set(node.value?.vid, value);
    node.children.forEach(c => this.setGlobalExpandValue(c, value))
  }

  isContentShowed(): boolean {
    let map = this.expandMap ? this.expandMap : this.wholeVersionService.expandMap;
    return map.get(this.node.value?.vid) === true;
  }

  isExpandButtonShowed(): boolean {
    return (this.node.value?.groupContent.length !== 0) ||
      ((this.node.value?.leafContent.length !== 0));
  }

}