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
      obs.pipe(
        switchMap(newGroupWithId => {
          const newGroup: GroupContent = {
            id: newGroupWithId.id,
            name: newGroupWithId.name,
            vid: newGroupWithId.vid,
            realNode: true,
            isEarliest: true,
            groupContent: [],
            leafContent: []
          };

          // TODO Тут должно быть по другому
          // this.wholeVersionService.addGroupToParent(newGroup, this.node.value.vid);
          return this.wholeVersionService.createNewGroup();
        })
      )
    );
  }

  handleNewLeaf(obs: Observable<NewLeafWithId>): void {
    this.preloaderService.wrap(
      obs.pipe(
        tap(newLeafWithId => {
          const newLeaf: LeafContent = {
            id: newLeafWithId.id,
            name: newLeafWithId.name,
            vid: newLeafWithId.vid,
            valueType: newLeafWithId.valueType,
            value: newLeafWithId.value,
          };
          this.wholeVersionService.addLeafToParent(newLeaf, this.node.value.vid);
        })
      )
    );
  }

  handleDeleteGroup(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.wholeVersionService.deleteGroup())
      )
    );
  }

  handleUpdateGroup(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.wholeVersionService.updateGroup())
      )
    );
  }

  handleMaterializeGroup(obs: Observable<Group>): void {
    this.preloaderService.wrap(
      obs.pipe(
        tap(group => this.wholeVersionService.materializeGroup(group))
      )
    );
  }

  handleDematerializeGroup(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.wholeVersionService.dematerializeGroup()),
      )
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
    const versionId = this.wholeVersionService.wholeVersion.id;
    const groupId = this.node.value.id;

    const dto = { changeAgainstId };

    this.preloaderService.wrap(
      this.http.patch(`http://localhost:8080/version/${versionId}/group/${groupId}/position`, dto)
        .pipe(
          switchMap(() => this.wholeVersionService.swapGroups(groupId, changeAgainstId))
        )
    );
  }
}
