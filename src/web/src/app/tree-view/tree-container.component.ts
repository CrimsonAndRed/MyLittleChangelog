import { Component, ContentChild, Input, TemplateRef } from '@angular/core';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'tree-container',
  templateUrl: './tree-container.component.html',
  styleUrls: ['./tree-container.component.scss']
})
export class TreeContainerComponent {
  @ContentChild('groupContainer', { static: false }) groupContainerTemplateRef: TemplateRef<any>;

  @Input() node: TreeNode<any>;

  constructor() { }
}
