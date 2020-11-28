import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext, GroupHeader } from 'app/groups-sec/groups-sec.model';
import { GroupContent } from 'app/model/group-content';
import { WholeVersionService } from 'app/whole-version/whole-version.service';
import { Type } from '@angular/core';
import { GroupMovementGroupHeaderComponent } from './group-header/group-header.component';
import { GroupMovementGlobalHeaderComponent } from './global-header/global-header.component';

@Component({
  selector: 'group-movement',
  templateUrl: './group-movement.component.html',
  styleUrls: ['./group-movement.component.scss']
})
export class GroupMovementComponent implements OnInit {

  @Input() parentVid: number;
  @Input() group: GroupContent;
  @Output() parentChange = new EventEmitter<number>();
  config: GroupsSecConfig

  context: GroupsSecContext;

  constructor(public wholeVersionService: WholeVersionService) { }

  ngOnInit(): void {
    this.config = new GroupSecConfigBuilder()
      .setGroupHeader(GroupMovementGroupHeaderComponent)
      .setGlobalHeader(GroupMovementGlobalHeaderComponent)
      .setLeafShowCondition(() => false)
      .build();

    let set: Set<number> = new Set();
    set.add(this.parentVid);
    this.collectChildVids(set, this.group);

    this.context = {
      parentVid: this.parentVid,
      emitGroupChoice: this.onGroupChoice.bind(this),
      group: this.group,
      forbiddenVids: set
    };
  }

  onGroupChoice(group: GroupContent): void {
    this.parentChange.emit(group?.vid);
  }

  collectChildVids(set: Set<number>, group: GroupContent) {
    set.add(group.vid);
    group.groupContent.forEach(it => {
      this.collectChildVids(set, it);
    })
  }
}
