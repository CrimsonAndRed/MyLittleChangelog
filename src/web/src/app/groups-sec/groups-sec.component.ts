import { Component, ComponentFactoryResolver, Input, OnInit, ViewChild, ViewChildren } from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { GroupLeavesSecComponent } from './group-leaves-sec/group-leaves-sec.component';
import { GlobalHeaderDr } from './groups-sec.directive';

import {
  GlobalHeader,
  GroupsSecConfig,
  GroupsSecContext,
} from './groups-sec.model';

@Component({
  selector: 'groups-sec',
  templateUrl: './groups-sec.component.html',
  styleUrls: ['./groups-sec.component.scss']
})
export class GroupsSecComponent implements OnInit {

  @Input() groups: GroupContent[];
  @Input() config: GroupsSecConfig;
  @Input() context: GroupsSecContext;

  @ViewChild(GlobalHeaderDr, {static: true}) globalHeader: GlobalHeaderDr;
  @ViewChildren(GroupLeavesSecComponent) embeddedGroupLeaves: GroupLeavesSecComponent[];

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    if (this.config?.globalHeader) {
      const header = this.config.globalHeader;
      const factory = this.componentFactoryResolver.resolveComponentFactory(header);
      const viewContainerRef = this.globalHeader.viewContainerRef;
      const componentRef = viewContainerRef.createComponent<GlobalHeader>(factory);
      componentRef.instance.data = {
        groups: this.groups,
      };
      componentRef.instance.ctx = this.context;
    }
  }

  changeGlobalContentShow(value: boolean): void {
    this.config.expandMap.set(null, value);
    if (value === true) {
      this.embeddedGroupLeaves.forEach(egl => egl.changeGlobalContentShow(true));
    }
  }

  changeLocalContentShow(value: boolean): void {
    this.config.expandMap.set(null, value);
  }

  isContentShowed(): boolean {
    return this.config.expandMap.get(null) === true;
  }
}
