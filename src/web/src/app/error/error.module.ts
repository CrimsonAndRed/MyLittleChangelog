import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ErrorModalComponent } from './error-modal/error-modal.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    ErrorModalComponent,
  ],
  imports: [
    BrowserModule,
    MatDialogModule
  ]
})
export class ErrorModule { }
