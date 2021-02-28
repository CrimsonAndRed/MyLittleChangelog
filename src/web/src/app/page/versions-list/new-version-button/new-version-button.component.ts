import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { VersionsListService } from '../versions-list.service';
import { NewVersionModalComponent } from './new-version-modal/new-version-modal.component';

@Component({
  selector: 'new-version-button',
  templateUrl: './new-version-button.component.html',
  styleUrls: ['./new-version-button.component.scss']
})
export class NewVersionButtonComponent {

  constructor(private versionsListService: VersionsListService,
              private dialog: MatDialog) {
  }


  onNewVersionButtonClick(): void {

    const dialogRef = this.dialog.open(NewVersionModalComponent, {
      hasBackdrop: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.versionsListService.createVersion(result)
      }
    });
  }
}
