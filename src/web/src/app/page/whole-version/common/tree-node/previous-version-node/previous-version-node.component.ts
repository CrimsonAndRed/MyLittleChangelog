import { Component, Input, OnInit } from '@angular/core';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/service/http.service';
import { TreeNode } from 'app/model/tree';
import { GroupContent } from 'app/model/group-content';
import { PastGroupContent, PastLeafContent, PastRadioEvent, PreviousUsedGroupsAndLeaves } from 'app/model/previous-version';
import { Subject } from 'rxjs';
import { LeafContent } from 'app/model/leaf-content';

@Component({
  selector: 'previous-version-node',
  templateUrl: './previous-version-node.component.html',
  styleUrls: ['./previous-version-node.component.scss']
})
export class PreviousVersionNodeComponent {

  @Input() node: TreeNode<PastGroupContent>;
  @Input() expandMap: Map<number, boolean>;
  @Input() nodeCheckSubject: Subject<PastRadioEvent>;

  constructor(private wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService,
              private http: Http) { }

  showGroupInput(): boolean {
    return !this.node.value.inCurrentVersion;
  }

  showLeafInput(leaf: PastLeafContent): boolean {
    return !leaf.inCurrentVersion;
  }
  changeGlobalContentShow(value: boolean): void {
    this.setGlobalExpandValue(this.node, value);
  }

  changeLocalContentShow(value: boolean): void {
    this.setLocalExpandValue(this.node, value);
  }

  setLocalExpandValue(node: TreeNode<GroupContent>, value: boolean) {
    this.expandMap.set(node.value.vid, value);
  }

  setGlobalExpandValue(node: TreeNode<GroupContent>, value: boolean) {
    this.expandMap.set(node.value.vid, value);
    node.children.forEach(c => this.setGlobalExpandValue(c, value))
  }

  isContentShowed(): boolean {
    return this.expandMap.get(this.node.value.vid) === true;
  }

  isExpandButtonShowed(): boolean {
    return (this.node.value.groupContent.length !== 0) ||
      ((this.node.value.leafContent.length !== 0));
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
