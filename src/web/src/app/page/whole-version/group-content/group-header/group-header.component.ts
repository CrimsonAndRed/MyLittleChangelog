import { Component, EventEmitter, Input } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, NewLeafWithId } from 'app/model/leaf-content';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/service/http.service';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class GroupHeaderComponent {

  @Input() node: TreeNode<GroupContent>

  constructor(private wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService,
              private http: Http) { }

  handleNewGroup(obs: Observable<Group>): void {
    this.preloaderService.wrap(
      obs
    );
  }

  handleNewLeaf(obs: Observable<NewLeafWithId>): void {
    this.preloaderService.wrap(
      obs
    );
  }

  handleDeleteGroup(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs
    );
  }

  handleUpdateGroup(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs
    );
  }

  handleMaterializeGroup(obs: Observable<Group>): void {
    this.preloaderService.wrap(
      obs
    );
  }

  handleDematerializeGroup(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs
    );
  }

  isUpButtonShowed(): boolean {
    if (!this.node.value.realNode) {
      return false;
    }

    return this.node.parent.children[0].value.id !== this.node.value.id;
  }

  isDownButtonShowed(): boolean {
    if (!this.node.value.realNode) {
      return false;
    }
    return this.node.parent.children[this.node.parent.children.length - 1].value.id !== this.node.value.id;
  }

  handleMoveUp(): void {
    this.moveGroup(this.node.parent.children[this.findIdxInParent() - 1].value.id);
  }

  handleMoveDown(): void {
    this.moveGroup(this.node.parent.children[this.findIdxInParent() + 1].value.id);
  }

  private findIdxInParent(): number {
    return this.node.parent.children.findIndex(l => l.value.id === this.node.value.id);
  }

  private moveGroup(changeAgainstId: number): void {
    const groupId = this.node.value.id;

    const dto = { changeAgainstId };

    this.preloaderService.wrap(
      this.wholeVersionService.moveGroup(dto, groupId)
    );
  }
}
