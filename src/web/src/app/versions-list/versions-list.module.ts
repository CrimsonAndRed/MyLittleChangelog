import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { VersionsListComponent } from './versions-list.component';
import { NewVersionComponent } from './new-version/new-version.component';
import { DifferenceButtonComponent } from './difference-button/difference-button.component';
import { DifferenceModalComponent } from './difference-button/difference-modal/difference-modal.component';

import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    VersionsListComponent,
    NewVersionComponent,
    DifferenceButtonComponent,
    DifferenceModalComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
  ],
})
export class VersionsListModule { }
