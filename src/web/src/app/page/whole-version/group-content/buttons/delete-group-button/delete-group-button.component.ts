import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'delete-group-button',
  templateUrl: './delete-group-button.component.html',
  styleUrls: ['./delete-group-button.component.scss']
})
export class DeleteGroupButtonComponent {

  @Input() node: TreeNode<GroupContent>;
  @Output() onDeleteGroup = new EventEmitter<Observable<void>>();

  constructor(private http: Http, private route: ActivatedRoute, private wholeVersionService: WholeVersionService) {}

  onDeleteClick(): void {
    const versionId = this.wholeVersionService.wholeVersion.id;
    const groupId = this.node.value.id;
    const params = new HttpParams().set('hierarchy', 'true');

    this.onDeleteGroup.emit(this.http.delete(`http://localhost:8080/version/${versionId}/group/${groupId}`, params));
  }
}
