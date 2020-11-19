import { Component, OnInit, Input, Inject } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/http/http.service';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';

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

  onToVersionChoose(version: Version) {
    this.toVersion = version;
  }

  onFromVersionChoose(version: Version) {
    this.fromVersion = version;
  }

  onGoToDifference() {
    this.dialogRef.close(null);
    this.router.navigate(['difference'], { queryParams: { 'to': this.toVersion.id, 'from': this.fromVersion.id }, skipLocationChange: false});
  }
}
