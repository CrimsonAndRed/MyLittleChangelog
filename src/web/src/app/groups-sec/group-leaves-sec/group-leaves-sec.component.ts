import {
  Component,
  Input,
  Output,
  ViewChild,
  ViewChildren,
  ComponentFactoryResolver,
  Type,
  OnInit,
  EventEmitter,
} from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { GroupHeaderDr } from '../groups-sec.directive';

import { GroupHeader, LeafHeader, ParentGroupListChangeFn, GroupChangeFn } from '../groups-sec.model';

@Component({
  selector: 'group-leaves-sec',
  templateUrl: './group-leaves-sec.component.html',
  styleUrls: ['./group-leaves-sec.component.scss']
})
export class GroupLeavesSecComponent implements OnInit {

  @Input() group: GroupContent;
  @Input() parentGroup: GroupContent = null;
  @Input() leaves: LeafContent[] = null;
  @Input() groupHeaderRef: Type<GroupHeader> = null;
  @Input() leafHeaderRef: Type<LeafHeader> = null;
  @Input() isContentShowed: boolean = false;

  @Output() onParentChange = new EventEmitter<ParentGroupListChangeFn>();

  @ViewChild(GroupHeaderDr, {static: true}) header: GroupHeaderDr;
  @ViewChildren(GroupLeavesSecComponent) embeddedGroupLeaves: GroupLeavesSecComponent[];

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { };

  ngOnInit() {
    if (this.groupHeaderRef) {
      const factory = this.componentFactoryResolver.resolveComponentFactory(this.groupHeaderRef);
      const viewContainerRef = this.header.viewContainerRef;
      const componentRef = viewContainerRef.createComponent<GroupHeader>(factory);
      componentRef.instance.group = this.group;
      componentRef.instance.parentGroup = this.parentGroup;
      componentRef.instance.onGroupChange.subscribe(g => this.group = g);
      componentRef.instance.onParentGroupsChange.subscribe(fn => this.onParentChange.emit(fn));
    }
  }

  onGroupListChange(fn: ParentGroupListChangeFn) {
    this.group.groupContent = fn(this.group.groupContent);
  }

  handleGroupChange(fn: GroupChangeFn) {
    this.group = fn(this.group);
  }

  setContentShowed(value: boolean) {
    this.isContentShowed = value;
    if (value === true) {
      this.embeddedGroupLeaves.forEach(egl => egl.setContentShowed(true));
    }
  }

}
