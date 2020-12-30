import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'materialize-group-button',
  templateUrl: './materialize-group-button.component.html',
  styleUrls: ['./materialize-group-button.component.scss']
})
export class MaterializeGroupButtonComponent {

  @Input() node: TreeNode<GroupContent>;
  @Output() onMaterializeGroup = new EventEmitter<Observable<void>>();

  constructor(private wholeVersionService: WholeVersionService) {}

  onMaterializeClick(): void {
    const newGroup: NewGroup = {
      vid: this.node.value.vid,
      name: this.node.value.name,
      parentVid: this.node.parent?.value?.vid,
    };
    this.onMaterializeGroup.emit(this.wholeVersionService.materializeGroup(newGroup));
  }
}
