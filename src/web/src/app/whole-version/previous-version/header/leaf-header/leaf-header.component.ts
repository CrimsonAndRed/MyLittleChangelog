import { Component } from '@angular/core';
import { GroupsSecContext, LeafHeader, LeafHeaderData } from 'app/groups-sec/groups-sec.model';
import { leafContentToPrevious } from '../../previous-version.model';

@Component({
  selector: 'leaf-header',
  templateUrl: './leaf-header.component.html',
  styleUrls: ['./leaf-header.component.scss']
})
export class LeafHeaderComponent implements LeafHeader {

  data: LeafHeaderData;
  ctx: GroupsSecContext;

  constructor() { }

  onNodeChecked(): void {
    this.ctx.emitLeafCheck(leafContentToPrevious(this.data.leaf, this.ctx.usedIds), this.data.parentGroup);
  }

  showRadioButton(): boolean {
    return !this.ctx.usedIds.usedLeaves.has(this.data.leaf.id)
  }
}
