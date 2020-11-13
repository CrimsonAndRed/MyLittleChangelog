import { Component, Output, EventEmitter, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';
import { GroupContent, NewGroup, Group } from 'app/model/group-content';
import { NewLeaf, NewLeafWithId } from 'app/model/leaf-content';
import { WholeVersion } from 'app/model/whole-version';
import { PreviousVersionModalComponent } from '../previous-version-modal/previous-version-modal.component';
import { PreviousUsedGroupsAndLeaves } from '../previous-version.model';
import { Observable } from 'rxjs';
import { switchMap, map } from 'rxjs/operators';

@Component({
  selector: 'previous-version-select-button',
  templateUrl: './previous-version-select-button.component.html',
  styleUrls: ['./previous-version-select-button.component.scss']
})
export class PreviousVersionSelectButtonComponent {

  @Output() nodeChosen = new EventEmitter<Observable<void>>();
  @Input() currentGroups: GroupContent[];

  constructor(
    private http: Http,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) { }

  onButtonClick(): void {
    const dialogRef = this.dialog.open(PreviousVersionModalComponent, {
      hasBackdrop: true,
      minWidth: '80%',
      data: {
        version: this.http.get<WholeVersion>('http://localhost:8080/version/previous'),
        currentGroups: this.currentGroups,
      }
    });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.handleResult(res);
      }
    });
  }

  handleResult(result: any): void {
    switch (result?.kind) {
      case 'group':
        this.addGroupFromPast({
          vid: result.value.vid,
          name: result.value.name,
          parentVid: result.parentVid
        });
        break;
      case 'leaf':
        this.addLeafFromPast({
          vid: result.value.vid,
          name: result.value.name,
          value: result.value.value,
          valueType: result.value.valueType
        }, result.parentId);
        break;
      default:
        break;
    }
  }

  private addGroupFromPast(newGroup: NewGroup): void {
    const versionId = this.route.snapshot.params.id;

    this.nodeChosen.emit(
      this.http.post<Group>(`http://localhost:8080/version/${versionId}/group`, newGroup)
        .pipe(map((res) => { return; }))
    );
  }

  private addLeafFromPast(newLeaf: NewLeaf, parentId: number): void {
    const versionId = this.route.snapshot.params.id;

    this.nodeChosen.emit(
      this.http.post<NewLeafWithId>(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf`, newLeaf)
        .pipe(map((res) => { return; }))
    );
  }
}
