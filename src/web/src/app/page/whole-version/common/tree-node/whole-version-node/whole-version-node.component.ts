import { Component, Input } from '@angular/core';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/service/http.service';
import { TreeNode } from 'app/model/tree';
import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'whole-version-node',
  templateUrl: './whole-version-node.component.html',
  styleUrls: ['./whole-version-node.component.scss']
})
export class WholeVersionNodeComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  @Input() node: TreeNode<GroupContent>;
  @Input() expandMap: Map<number, boolean>;

  constructor(private wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService,
              private http: Http) { }

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
}
