import { Component, EventEmitter } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, NewLeafWithId } from 'app/model/leaf-content';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { WholeVersionService } from 'app/service/whole-version.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { SpinnerService } from 'app/spinner/spinner.service';

@Component({
  selector: 'group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class GroupHeaderComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  constructor(private wholeVersionService: WholeVersionService, private spinnerService: SpinnerService) { }

  handleNewGroup(obs: Observable<Group>): void {
    this.spinnerService.wrapSpinner(
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
    this.spinnerService.wrapSpinner(
      obs.pipe(
        tap(newLeafWithId => {
          const newLeaf: LeafContent = {
            id: newLeafWithId.id,
            name: newLeafWithId.name,
            vid: newLeafWithId.vid,
            valueType: newLeafWithId.valueType,
            value: newLeafWithId.value,
            groupVid: newLeafWithId.groupVid
          };
          this.wholeVersionService.addLeafToParent(newLeaf, this.data.group.vid)
        })
      )
    );
  }

  handleDeleteGroup(obs: Observable<void>): void {
    this.spinnerService.wrapSpinner(
      obs.pipe(
        switchMap(() => this.wholeVersionService.deleteGroup(this.data.group.id, this.data.parentGroup?.vid))
      )
    );
  }

  handleUpdateGroup(obs: Observable<Group>): void {
    this.spinnerService.wrapSpinner(
      obs.pipe(
        tap(group => this.wholeVersionService.updateGroup(group))
      )
    );
  }

  handleMaterializeGroup(obs: Observable<Group>): void {
    this.spinnerService.wrapSpinner(
      obs.pipe(
        tap(group => this.wholeVersionService.materializeGroup(group))
      )
    );
  }

  handleDematerializeGroup(obs: Observable<Group>): void {
    this.spinnerService.wrapSpinner(
      obs.pipe(
        switchMap((group: Group) => this.wholeVersionService.dematerializeGroup(group)),
      )
    );
  }
}
