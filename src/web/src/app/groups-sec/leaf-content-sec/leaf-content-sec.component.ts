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
import { LeafHeader, GroupChangeFn } from '../groups-sec.model';
import { LeafHeaderDr } from '../groups-sec.directive';

@Component({
  selector: 'leaf-content-sec',
  templateUrl: './leaf-content-sec.component.html',
  styleUrls: ['./leaf-content-sec.component.scss']
})
export class LeafContentSecComponent implements OnInit {

  @Input() leafHeaderRef: Type<LeafHeader> = null;
  @Input() leaf: LeafContent;
  @Input() groupId: number;

  @Output() onParentChange = new EventEmitter<GroupChangeFn>();

  @ViewChild(LeafHeaderDr, {static: true}) header: LeafHeaderDr;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit() {
    if (this.leafHeaderRef) {
      const factory = this.componentFactoryResolver.resolveComponentFactory(this.leafHeaderRef);
      const viewContainerRef = this.header.viewContainerRef;
      const componentRef = viewContainerRef.createComponent<LeafHeader>(factory);
      componentRef.instance.leaf = this.leaf;
      componentRef.instance.groupId = this.groupId;
      componentRef.instance.onLeafChange.subscribe(l => this.leaf = l);
      componentRef.instance.onParentChange.subscribe(fn => this.onParentChange.emit(fn));
    }
  }

//  onEditButtonClick() {
//    const dialogRef = this.dialog.open(EditLeafModalComponent, {
//      hasBackdrop: true,
//      data: {
//        leaf: this.leafContent,
//        groups: this.groups
//      }
//    });
//
//    dialogRef.afterClosed().subscribe(result => {
//      if (result) {
//        this.updateLeaf(result);
//      }
//    });
//
//  }
//
//  onDeleteButtonClick() {
//    const versionId = this.route.snapshot.data.version.id;
//    const groupId = this.parentId;
//    const leafId = this.leafContent.id;
//
//    this.http.delete(`http://localhost:8080/version/${versionId}/group/${groupId}/leaf/${leafId}`)
//      .subscribe(() => this.onLeafDelete.emit(this.leafContent));
//  }
//
//  updateLeaf(leaf: LeafContent) {
//    const versionId = this.route.snapshot.data.version.id;
//    const parentId = this.parentId;
//    const leafId = leaf.id;
//
//    const leafToUpdate: LeafToUpdate = {
//      name: leaf.name,
//      valueType: leaf.valueType,
//      value: leaf.value,
//      parentId: parentId,
//    }
//    this.http.put<UpdatedLeaf>(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf/${leafId}`, leafToUpdate)
//          .subscribe(updatedLeaf => this.onLeafUpdate.emit(updatedLeaf));
//  }

}
