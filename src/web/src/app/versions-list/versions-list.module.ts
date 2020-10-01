import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { VersionsListComponent } from './versions-list.component';
import { NewVersionComponent } from './new-version/new-version.component';

import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    VersionsListComponent,
    NewVersionComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
  ],
})
export class VersionsListModule { }
