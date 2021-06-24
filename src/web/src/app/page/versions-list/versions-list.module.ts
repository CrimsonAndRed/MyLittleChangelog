import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { VersionsListComponent } from './versions-list.component';
import { NewVersionButtonComponent } from './new-version-button/new-version-button.component';
import { DifferenceButtonComponent } from './difference-button/difference-button.component';
import { DifferenceModalComponent } from './difference-button/difference-modal/difference-modal.component';

import { HttpClientModule } from '@angular/common/http';
import { MatSelectModule } from '@angular/material/select';
import { NewVersionModalComponent } from './new-version-button/new-version-modal/new-version-modal.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    VersionsListComponent,
    NewVersionButtonComponent,
    DifferenceButtonComponent,
    DifferenceModalComponent,
    NewVersionModalComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatSelectModule,
    MatDialogModule
  ],
})
export class VersionsListModule { }
