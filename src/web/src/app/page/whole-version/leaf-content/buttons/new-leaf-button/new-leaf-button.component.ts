import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Http } from 'app/service/http.service';
import { NewLeafWithId, NewLeaf} from 'app/model/leaf-content';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { NewLeafModalComponent } from './new-leaf-modal/new-leaf-modal.component';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { GroupContent } from 'app/model/group-content';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'new-leaf-button',
  templateUrl: './new-leaf-button.component.html',
  styleUrls: ['./new-leaf-button.component.scss']
})
export class NewLeafButtonComponent {

  @Output() onNewLeaf = new EventEmitter<Observable<NewLeafWithId>>();

  @Input() node: TreeNode<GroupContent>;

  constructor(private http: Http,
              private dialog: MatDialog,
              private wholeVersionService: WholeVersionService) {
  }

  onNewLeafButtonClick(): void {

    const dialogRef = this.dialog.open(NewLeafModalComponent, {
      hasBackdrop: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.createNewLeaf(result);
      }
    });
  }

  createNewLeaf(newLeaf: NewLeaf): void {
    const versionId = this.wholeVersionService.wholeVersionHeader.id;
    const groupId = this.node.value.id;

    this.onNewLeaf.emit(
      this.http.post<NewLeafWithId>(`http://localhost:8080/version/${versionId}/group/${groupId}/leaf`, newLeaf)
    );
  }

}
