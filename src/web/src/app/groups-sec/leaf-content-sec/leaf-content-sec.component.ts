import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  Type,
  ViewChild,
  ComponentFactoryResolver,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LeafContent } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';
import { LeafHeader, GroupsSecContext, GroupsSecConfig } from '../groups-sec.model';
import { LeafHeaderDr } from '../groups-sec.directive';
import { LeafTypeService } from 'app/service/leaf-type.service';

@Component({
  selector: 'leaf-content-sec',
  templateUrl: './leaf-content-sec.component.html',
  styleUrls: ['./leaf-content-sec.component.scss']
})
export class LeafContentSecComponent implements OnInit {

  @Input() config: GroupsSecConfig = null;
  @Input() context: GroupsSecContext = null;
  @Input() leaf: LeafContent;
  @Input() parentGroup: GroupContent;

  @ViewChild(LeafHeaderDr, {static: true}) header: LeafHeaderDr;

  constructor(private componentFactoryResolver: ComponentFactoryResolver, public leafTypeService: LeafTypeService) { }

  ngOnInit(): void {
    if (this.config?.leafHeader) {
      const header = this.config.leafHeader;
      const factory = this.componentFactoryResolver.resolveComponentFactory(header);
      const viewContainerRef = this.header.viewContainerRef;
      const componentRef = viewContainerRef.createComponent<LeafHeader>(factory);
      componentRef.instance.data = {
        leaf: this.leaf,
        parentGroup: this.parentGroup
      };
      componentRef.instance.ctx = this.context;
    }
  }
}
