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
import { LeafDifference } from 'app/model/difference';
import { GroupContent } from 'app/model/group-content';
import { LeafTypeService } from 'app/service/leaf-type.service';

@Component({
  selector: 'leaf-third',
  templateUrl: './leaf-third.component.html',
  styleUrls: ['./leaf-third.component.scss']
})
export class LeafThirdComponent {

  @Input() leaf: LeafDifference;

  constructor(public leafTypeService: LeafTypeService) { }
}
