import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LeafHeader, GroupChangeFn } from 'app/groups-sec/groups-sec.model';
import { LeafContent, UpdatedLeaf } from 'app/model/leaf-content';

@Component({
  selector: 'leaf-header',
  templateUrl: './leaf-header.component.html',
  styleUrls: ['./leaf-header.component.scss']
})
export class LeafHeaderComponent implements LeafHeader {

  leaf: LeafContent;
  groupId: number;
  onLeafChange = new EventEmitter<LeafContent>();
  onParentChange = new EventEmitter<GroupChangeFn>();

  constructor() {}

  handleDeleteLeaf() {
    this.onParentChange.emit(g => {
      g.leafContent = g.leafContent.filter(l => l.id !== this.leaf.id);
      return g;
    });
  }

  handleUpdateLeaf(updatedLeaf: UpdatedLeaf) {
    this.leaf.name = updatedLeaf.name;
    this.leaf.valueType = updatedLeaf.valueType;
    this.leaf.value = updatedLeaf.value;

    this.onLeafChange.emit(this.leaf);
  }

}
