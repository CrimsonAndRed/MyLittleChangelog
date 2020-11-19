import { Component, OnInit, Input } from '@angular/core';
import { Version } from 'app/model/version';
import { PreloaderService } from 'app/preloader/preloader.service';
import { DifferenceModalComponent } from './difference-modal/difference-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'difference-button',
  templateUrl: './difference-button.component.html',
  styleUrls: ['./difference-button.component.scss']
})
export class DifferenceButtonComponent  {

  @Input() versions: Version[];

  constructor(private dialog: MatDialog) {
  }

  onDifferenceButtonClick(): void {
    const dialogRef = this.dialog.open(DifferenceModalComponent, {
      hasBackdrop: true,
      minWidth: '80%',
      data: {
        versions: this.versions,
      }
    });
  }
}
