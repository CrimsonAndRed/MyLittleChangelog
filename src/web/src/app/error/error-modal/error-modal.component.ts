import { Component, Input, Inject } from '@angular/core';

import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'error-modal',
  templateUrl: './error-modal.component.html',
  styleUrls: ['./error-modal.component.scss']
})
export class ErrorModalComponent {

  errors: string[];

  constructor(@Inject(MAT_DIALOG_DATA) private errorData: ErrorData) {
    this.errors = errorData.errors;
  }
}

export interface ErrorData {
  errors: string[];
}
