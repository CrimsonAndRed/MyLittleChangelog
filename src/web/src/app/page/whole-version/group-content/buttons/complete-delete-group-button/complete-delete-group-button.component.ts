import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { LeafContent } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'complete-delete-group-button',
  templateUrl: './complete-delete-group-button.component.html',
  styleUrls: ['./complete-delete-group-button.component.scss']
})
export class CompleteDeleteGroupButtonComponent {

  @Input() node: TreeNode<GroupContent>;

  @Output() onDeleteGroup = new EventEmitter<void>();

  constructor(private http: Http, private route: ActivatedRoute, private wholeVersionService: WholeVersionService) {}

  onDeleteClick(): void {
    this.onDeleteGroup.emit();
  }

}
