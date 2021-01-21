import { Component } from '@angular/core';

import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'new-group-modal',
  templateUrl: './new-group-modal.component.html',
  styleUrls: ['./new-group-modal.component.scss']
})
export class NewGroupModalComponent {

  name = '';

  constructor(private dialogRef: MatDialogRef<NewGroupModalComponent>) {
  }
}
