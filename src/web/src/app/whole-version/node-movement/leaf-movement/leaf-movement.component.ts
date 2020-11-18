import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext, GroupHeader } from 'app/groups-sec/groups-sec.model';
import { GroupContent } from 'app/model/group-content';
import { WholeVersionService } from 'app/whole-version/whole-version.service';
import { LeafMovementGroupHeaderComponent } from './group-header/group-header.component';

@Component({
  selector: 'leaf-movement',
  templateUrl: './leaf-movement.component.html',
  styleUrls: ['./leaf-movement.component.scss']
})
export class LeafMovementComponent implements OnInit {

  @Input() parentVid: number;
  @Output() parentChange = new EventEmitter<number>();
  config: GroupsSecConfig

  context: GroupsSecContext;

  constructor(public wholeVersionService: WholeVersionService) { }

  ngOnInit(): void {
    this.config = new GroupSecConfigBuilder()
      .setGroupHeader(LeafMovementGroupHeaderComponent)
      .setLeafShowCondition(() => false)
      .build();
    this.context = {
      parentVid: this.parentVid,
      emitGroupChoice: this.onGroupChoice.bind(this),
    };
  }

  onGroupChoice(group: GroupContent): void {
    this.parentChange.emit(group.vid);
  }
}
