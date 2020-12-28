import { Component, EventEmitter, Input } from '@angular/core';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { TreeNode } from 'app/model/tree';
import { LeafTypeService } from 'app/service/leaf-type.service';

@Component({
  selector: 'leaf-body',
  templateUrl: './leaf-body.component.html',
  styleUrls: ['./leaf-body.component.scss']
})
export class LeafBodyComponent {

  @Input() leaf: LeafContent;

  constructor(public leafTypeService: LeafTypeService) { }
}
