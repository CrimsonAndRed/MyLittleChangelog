import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LeafHeader, LeafHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupContent } from 'app/model/group-content';
import { LeafContent, UpdatedLeaf } from 'app/model/leaf-content';
import { WholeVersionService } from 'app/service/whole-version.service';

@Component({
  selector: 'leaf-header',
  templateUrl: './leaf-header.component.html',
  styleUrls: ['./leaf-header.component.scss']
})
export class LeafHeaderComponent implements LeafHeader {

  data: LeafHeaderData;
  ctx: GroupsSecContext;

  constructor(private wholeVersionService: WholeVersionService) {}

  handleDeleteLeaf(): void {
    this.wholeVersionService.deleteLeaf(this.data.leaf.id, this.data.parentGroup.vid);
  }

  handleUpdateLeaf(updatedLeaf: UpdatedLeaf): void {
    this.wholeVersionService.updateLeaf(updatedLeaf, this.data.parentGroup.vid);
  }
}
