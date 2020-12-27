import { Component } from '@angular/core';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';

@Component({
  selector: 'leaf-movement-group-header',
  templateUrl: './group-header.component.html',
  styleUrls: ['./group-header.component.scss']
})
export class LeafMovementGroupHeaderComponent implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  constructor() { }

  onNodeCheckRadioClick(): void {
    this.ctx.emitGroupChoice(this.data.group);
  }

  isRadioButtonShowed(): boolean {
    return this.ctx.parentVid !== this.data.group.vid;
  }
}
