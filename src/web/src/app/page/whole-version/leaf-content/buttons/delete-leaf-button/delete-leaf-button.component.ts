import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { LeafContent } from 'app/model/leaf-content';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'delete-leaf-button',
  templateUrl: './delete-leaf-button.component.html',
  styleUrls: ['./delete-leaf-button.component.scss']
})
export class DeleteLeafButtonComponent {

  @Input() node: TreeNode<GroupContent>;
  @Input() leaf: LeafContent;
  @Output() onDeleteLeaf = new EventEmitter<Observable<void>>();

  constructor(private http: Http, private route: ActivatedRoute, private wholeVersionService: WholeVersionService) {}

  onDeleteClick(): void {
    const versionId = this.wholeVersionService.wholeVersion.id;
    const groupId = this.node.value.id;
    const leafId = this.leaf.id;

    this.onDeleteLeaf.emit(this.http.delete(`http://localhost:8080/version/${versionId}/group/${groupId}/leaf/${leafId}`));
  }

}
