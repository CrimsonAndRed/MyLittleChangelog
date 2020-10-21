import {
  Component,
  Input,
  ViewChild,
  ElementRef,
  ComponentFactoryResolver,
  Type,
  OnInit,
} from '@angular/core';
import { GroupContent } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { GroupHeaderDr } from '../groups-sec.directive';

import { GroupHeader } from '../groups-sec.model';

@Component({
  selector: 'group-leaves-sec',
  templateUrl: './group-leaves-sec.component.html',
  styleUrls: ['./group-leaves-sec.component.scss']
})
export class GroupLeavesSec implements OnInit {

  @Input() group: GroupContent;
  @Input() leaves: LeafContent[] = null;
  @Input() groupHeaderRef: Type<GroupHeader> = null;

  @ViewChild(GroupHeaderDr, {static: true}) header: GroupHeaderDr;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { };

  ngOnInit() {
    if (this.groupHeaderRef) {
      const factory = this.componentFactoryResolver.resolveComponentFactory(this.groupHeaderRef);
      const viewContainerRef = this.header.viewContainerRef;
      const componentRef = viewContainerRef.createComponent<GroupHeader>(factory);
      componentRef.instance.group = this.group;
      componentRef.instance.onGroupChange.subscribe(g => this.group = g);
    }
  }

}
