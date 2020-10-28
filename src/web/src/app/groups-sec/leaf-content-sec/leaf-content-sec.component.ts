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
import { LeafHeader, GroupChangeFn, GroupsSecContext, GroupsSecConfig } from '../groups-sec.model';
import { LeafHeaderDr } from '../groups-sec.directive';

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

  @Output() onParentChange = new EventEmitter<GroupChangeFn>();

  @ViewChild(LeafHeaderDr, {static: true}) header: LeafHeaderDr;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    if (this.config?.leafHeader) {
      const header = this.config.leafHeader;
      const factory = this.componentFactoryResolver.resolveComponentFactory(header);
      const viewContainerRef = this.header.viewContainerRef;
      const componentRef = viewContainerRef.createComponent<LeafHeader>(factory);
      componentRef.instance.data = {
        leaf: this.leaf,
        parentGroup: this.parentGroup,
        leafChange: new EventEmitter<LeafContent>(),
        parentChange: new EventEmitter<GroupChangeFn>(),
      };
      componentRef.instance.ctx = this.context;
      componentRef.instance.data.leafChange.subscribe(l => this.leaf = l);
      componentRef.instance.data.parentChange.subscribe(fn => this.onParentChange.emit(fn));
    }
  }

}
