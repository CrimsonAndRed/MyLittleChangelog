import { Component, Input, Output, EventEmitter } from '@angular/core';
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
  @Output() onDeleteGroup = new EventEmitter<number>();

  constructor(private wholeVersionService: WholeVersionService) {}

  onDeleteClick(): void {
    this.onDeleteGroup.emit(this.node.value.id);
  }
}
