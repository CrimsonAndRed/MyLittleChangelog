import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { LeafContent } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'complete-delete-leaf-button',
  templateUrl: './complete-delete-leaf-button.component.html',
  styleUrls: ['./complete-delete-leaf-button.component.scss']
})
export class CompleteDeleteLeafButtonComponent {

  @Input() node: TreeNode<GroupContent>;
  @Input() leaf: LeafContent;
  @Output() onDeleteLeaf = new EventEmitter<void>();

  constructor(private http: Http, private route: ActivatedRoute, private wholeVersionService: WholeVersionService) {}

  onDeleteClick(): void {
    const groupVid = this.node.value.vid;
    const leafId = this.leaf.id;

    this.onDeleteLeaf.emit();
  }

}
