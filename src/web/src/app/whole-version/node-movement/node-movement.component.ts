import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupContent } from 'app/model/group-content';
import { NodeMovementGroupHeaderComponent } from './group-header/group-header.component';
import { WholeVersionService } from 'app/whole-version/whole-version.service';

@Component({
  selector: 'node-movement',
  templateUrl: './node-movement.component.html',
  styleUrls: ['./node-movement.component.scss']
})
export class NodeMovementComponent implements OnInit {

  @Input() parentVid: number;
  @Output() parentChange = new EventEmitter<number>();

  config: GroupsSecConfig = new GroupSecConfigBuilder()
    .setGroupHeader(NodeMovementGroupHeaderComponent)
    .setLeafShowCondition(() => false)
    .build();

  context: GroupsSecContext;

  constructor(public wholeVersionService: WholeVersionService) { }

  ngOnInit(): void {
    this.context = {
      parentVid: this.parentVid,
      emitGroupChoice: this.onGroupChoice.bind(this),
    };
  }

  onGroupChoice(group: GroupContent): void {
    this.parentChange.emit(group.vid);
  }
}
