import { Component, EventEmitter } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, NewLeafWithId } from 'app/model/leaf-content';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { WholeVersionService } from 'app/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/service/http.service';

@Component({
  selector: 'group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class GroupHeaderComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  constructor(private wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService,
              private http: Http) { }

  handleNewGroup(obs: Observable<Group>): void {
    this.preloaderService.wrap(
      obs.pipe(
        tap(newGroupWithId => {
          const newGroup: GroupContent = {
            id: newGroupWithId.id,
            name: newGroupWithId.name,
            vid: newGroupWithId.vid,
            realNode: true,
            isEarliest: true,
            groupContent: [],
            leafContent: []
          };

          this.wholeVersionService.addGroupToParent(newGroup, this.data.group.vid);
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
          this.wholeVersionService.addLeafToParent(newLeaf, this.data.group.vid);
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
    if (!this.data.group.realNode) {
      return false;
    }
    if (this.data.parentGroup == null) {
      return this.ctx.allGroups[0].id !== this.data.group.id;
    } else {
      return this.data.parentGroup.groupContent[0].id !== this.data.group.id;
    }
  }

  isDownButtonShowed(): boolean {
    if (!this.data.group.realNode) {
      return false;
    }
    if (this.data.parentGroup == null) {
      return this.ctx.allGroups[this.ctx.allGroups.length - 1].id !== this.data.group.id;
    } else {
      return this.data.parentGroup.groupContent[this.data.parentGroup.groupContent.length - 1].id !== this.data.group.id;
    }
  }

  handleMoveUp(): void {
    if (this.data.parentGroup == null) {
      this.moveGroup(this.ctx.allGroups[this.findIdxInParent() - 1].id);
    } else {
      this.moveGroup(this.data.parentGroup.groupContent[this.findIdxInParent() - 1].id);
    }
  }

  handleMoveDown(): void {
    if (this.data.parentGroup == null) {
      this.moveGroup(this.ctx.allGroups[this.findIdxInParent() + 1].id);
    } else {
      this.moveGroup(this.data.parentGroup.groupContent[this.findIdxInParent() + 1].id);
    }
  }

  private findIdxInParent(): number {
    if (this.data.parentGroup == null) {
      return this.ctx.allGroups.findIndex(l => l.id === this.data.group.id);
    } else {
      return this.data.parentGroup.groupContent.findIndex(l => l.id === this.data.group.id);
    }
  }

  private moveGroup(changeAgainstId: number): void {
    const versionId = this.wholeVersionService.wholeVersion.id;
    const groupId = this.data.group.id;

    const dto = { changeAgainstId };

    this.preloaderService.wrap(
      this.http.patch(`http://localhost:8080/version/${versionId}/group/${groupId}/position`, dto)
        .pipe(
          switchMap(() => this.wholeVersionService.swapGroups(groupId, changeAgainstId))
        )
    );
  }
}
