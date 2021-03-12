import { Component, Input } from '@angular/core';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/service/http.service';
import { TreeNode } from 'app/model/tree';
import { GroupContent } from 'app/model/group-content';
import { LeafContent, LeafToUpdate } from 'app/model/leaf-content';


@Component({
  selector: 'leaf-header',
  templateUrl: './leaf-header.component.html',
  styleUrls: ['./leaf-header.component.scss']
})
export class LeafHeaderComponent {
  @Input() node: TreeNode<GroupContent>;
  @Input() leaf: LeafContent;

  constructor(private wholeVersionService: WholeVersionService) {}

  handleDeleteLeaf(): void {
    this.wholeVersionService.deleteLeaf(this.leaf.id, this.node.value.vid);
  }

  handleCompleteDeleteLeaf(): void {
    this.wholeVersionService.completeDeleteLeaf(this.leaf.id, this.node.value.vid);
  }

  handleUpdateLeaf(leaf: LeafToUpdate): void {
    this.wholeVersionService.updateLeaf(leaf, this.node.value.id, this.leaf.id);
  }

  isUpButtonShowed(): boolean {
    return this.node.value.leafContent[0].id !== this.leaf.id;
  }

  isDownButtonShowed(): boolean {
    return this.node.value.leafContent[this.node.value.leafContent.length - 1].id !== this.leaf.id;
  }

  handleMoveUp(): void {
    this.moveLeaf(this.node.value.leafContent[this.findIdxInParent() - 1].id);
  }

  handleMoveDown(): void {
    this.moveLeaf(this.node.value.leafContent[this.findIdxInParent() + 1].id);
  }

  private findIdxInParent(): number {
    return this.node.value.leafContent.findIndex(l => l.id === this.leaf.id);
  }

  private moveLeaf(changeAgainstId: number): void {
    const groupVid = this.node.value.vid;
    const leafId = this.leaf.id;
    const dto = { changeAgainstId };
    this.wholeVersionService.moveLeaf(leafId, groupVid, dto)
  }
}
