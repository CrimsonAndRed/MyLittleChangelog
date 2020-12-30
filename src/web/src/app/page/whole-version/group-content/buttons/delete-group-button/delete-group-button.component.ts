import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';
import { WholeVersion } from 'app/model/whole-version';

@Component({
  selector: 'delete-group-button',
  templateUrl: './delete-group-button.component.html',
  styleUrls: ['./delete-group-button.component.scss']
})
export class DeleteGroupButtonComponent {

  @Input() node: TreeNode<GroupContent>;
  @Output() onDeleteGroup = new EventEmitter<Observable<WholeVersion>>();

  constructor(private wholeVersionService: WholeVersionService) {}

  onDeleteClick(): void {
    this.onDeleteGroup.emit(this.wholeVersionService.deleteGroup(this.node.value.id));
  }
}
