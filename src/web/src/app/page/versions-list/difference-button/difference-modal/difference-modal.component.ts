import { Component, Inject } from '@angular/core';
import { Version } from 'app/model/version';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'difference-modal',
  templateUrl: './difference-modal.component.html',
  styleUrls: ['./difference-modal.component.scss']
})
export class DifferenceModalComponent  {

  versions: Version[];
  fromVersion: Version;
  toVersion: Version;

  constructor(private dialogRef: MatDialogRef<DifferenceModalComponent>, @Inject(MAT_DIALOG_DATA) private data, private router: Router) {
    this.versions = data.versions;
  }

  onGoToDifference() {
    this.dialogRef.close(null);
    this.router.navigate(['difference'], { queryParams: { 'to': this.toVersion.id, 'from': this.fromVersion.id }, skipLocationChange: false});
  }

  onToChanged(event: MatSelectChange) {
    if (this.fromVersion != null && this.fromVersion.id >= event.value.id) {
      this.fromVersion = null;
    }
  }

  onFromChanged(event: MatSelectChange) {
    if (this.toVersion != null && this.toVersion.id <= event.value.id) {
      this.toVersion = null;
    }
  }
}
