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
import { WholeVersion } from 'app/model/whole-version';

@Component({
  selector: 'new-leaf-button',
  templateUrl: './new-leaf-button.component.html',
  styleUrls: ['./new-leaf-button.component.scss']
})
export class NewLeafButtonComponent {

  @Output() onNewLeaf = new EventEmitter<Observable<WholeVersion>>();

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
    this.onNewLeaf.emit(
      this.wholeVersionService.createNewLeaf(newLeaf, this.node.value.id)
    );
  }

}
