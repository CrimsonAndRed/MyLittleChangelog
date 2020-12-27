import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MatSelectModule } from '@angular/material/select';

import { TreeContainerComponent } from './tree-container.component';
import { RootContainerDr, GroupContainerDr } from './tree-container.directive';

@NgModule({
  declarations: [
    TreeContainerComponent,
    RootContainerDr,
    GroupContainerDr
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatSelectModule,
  ],
  exports: [
    TreeContainerComponent,
    RootContainerDr,
    GroupContainerDr,
  ]
})
export class TreeContainerModule { }
