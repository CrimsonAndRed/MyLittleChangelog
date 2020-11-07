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

import { GroupHeader, GroupsSecConfig, GroupsSecContext } from '../groups-sec.model';

@Component({
  selector: 'group-leaves-sec',
  templateUrl: './group-leaves-sec.component.html',
  styleUrls: ['./group-leaves-sec.component.scss']
})
export class GroupLeavesSecComponent implements OnInit {

  @Input() group: GroupContent;
  @Input() parentGroup: GroupContent = null;
  @Input() leaves: LeafContent[] = null;
  @Input() config: GroupsSecConfig = null;
  @Input() context: GroupsSecContext = null;

  @ViewChild(GroupHeaderDr, {static: true}) header: GroupHeaderDr;
  @ViewChildren(GroupLeavesSecComponent) embeddedGroupLeaves: GroupLeavesSecComponent[];

  isContentShowed = false;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    if (this.config?.groupHeader) {
      const header = this.config.groupHeader;
      const factory = this.componentFactoryResolver.resolveComponentFactory(header);
      const viewContainerRef = this.header.viewContainerRef;
      const componentRef = viewContainerRef.createComponent<GroupHeader>(factory);
      componentRef.instance.data = {
        group: this.group,
        parentGroup: this.parentGroup,
      };
      componentRef.instance.ctx = this.context;
    }
  }

  changeGlobalContentShow(value: boolean): void {
    this.isContentShowed = value;
    if (value === true) {
      this.embeddedGroupLeaves.forEach(egl => egl.changeGlobalContentShow(true));
    }
  }

  isExpandButtonShowed(): boolean {
    return (this.group.groupContent.length !== 0) ||
      ((this.group.leafContent.length !== 0) && this.config.leafShowCondition());
  }

}
