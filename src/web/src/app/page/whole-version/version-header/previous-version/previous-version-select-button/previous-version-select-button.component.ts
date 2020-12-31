import { Component, Output, EventEmitter, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { GroupContent, NewGroup, Group } from 'app/model/group-content';
import { NewLeaf, NewLeafWithId } from 'app/model/leaf-content';
import { WholeVersion } from 'app/model/whole-version';
import { PreviousVersionModalComponent } from '../previous-version-modal/previous-version-modal.component';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';
import { PastGroupContent, PastLeafContent, PastRadioEvent } from 'app/model/previous-version';

@Component({
  selector: 'previous-version-select-button',
  templateUrl: './previous-version-select-button.component.html',
  styleUrls: ['./previous-version-select-button.component.scss']
})
export class PreviousVersionSelectButtonComponent {

  @Output() nodeChosen = new EventEmitter<Observable<void>>();

  constructor(
    private http: Http,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private preloaderService: PreloaderService,
    private wholeVersionService: WholeVersionService
  ) { }

  onButtonClick(): void {

    this.preloaderService.wrap(
      this.wholeVersionService.getPrevoiusVersion()
      .pipe(
        tap(v => {
          const dialogRef = this.dialog.open(PreviousVersionModalComponent, {
            hasBackdrop: true,
            minWidth: '80%',
            data: {
              version: v,
            }
          });

          dialogRef.afterClosed().subscribe(res => {
            if (res) {
              this.handleResult(res);
            }
          });
        })
      )
    );
  }

  handleResult(result: PastRadioEvent): void {
    switch (result?.kind) {
      case 'group':
        const value = result.value as TreeNode<PastGroupContent>;
        this.addGroupFromPast({
          vid: value.value.vid,
          name: value.value.name,
          parentVid: value.parent?.value?.vid
        });
        break;
      case 'leaf':
        const valueLeaf = result.value as PastLeafContent;
        this.addLeafFromPast({
          vid: valueLeaf.vid,
          name: valueLeaf.name,
          value: valueLeaf.value,
          valueType: valueLeaf.valueType
        }, result.parentId);
        break;
      default:
        break;
    }
  }

  private addGroupFromPast(newGroup: NewGroup): void {
    this.nodeChosen.emit(
      this.wholeVersionService.createNewGroup(newGroup)
        .pipe(map(() => {}))
    );
  }

  private addLeafFromPast(newLeaf: NewLeaf, parentId: number): void {
    this.nodeChosen.emit(
      this.wholeVersionService.createNewLeaf(newLeaf, parentId)
        .pipe(map(() => {}))
    );
  }
}
