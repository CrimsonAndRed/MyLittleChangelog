import { Component, Input } from '@angular/core';
import { LeafHeader, LeafHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/service/http.service';
import { TreeNode } from 'app/model/tree';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';


@Component({
  selector: 'leaf-header',
  templateUrl: './leaf-header.component.html',
  styleUrls: ['./leaf-header.component.scss']
})
export class LeafHeaderComponent implements LeafHeader {

  data: LeafHeaderData;
  ctx: GroupsSecContext;

  @Input() node: TreeNode<GroupContent>;
  @Input() leaf: LeafContent;

  constructor(private http: Http,
              private wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService) {}

  handleDeleteLeaf(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.wholeVersionService.deleteLeaf())
      )
    );
  }

  handleUpdateLeaf(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.wholeVersionService.updateLeaf())
      )
    );
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
    const versionId = this.wholeVersionService.wholeVersion.id;
    const parentId = this.node.value.id;
    const leafId = this.leaf.id;

    const dto = { changeAgainstId };

    this.preloaderService.wrap(
      this.http.patch(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf/${leafId}/position`, dto)
        .pipe(
          tap(() => this.wholeVersionService.swapLeaves(this.node.value.vid, leafId, changeAgainstId))
        )
    );
  }
}
