import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MatSelectModule } from '@angular/material/select';

import { TreeContainerComponent } from './tree-container.component';
import { RootContainerDr, GroupContainerDr } from './tree-container.directive';
import { ExpandBlockComponent } from './expand-block/expand-block.component';
import { ExpandAllButtonComponent } from './expand-block/expand-all-button/expand-all-button.component';
import { ExpandOneButtonComponent } from './expand-block/expand-one-button/expand-one-button.component';

@NgModule({
  declarations: [
    TreeContainerComponent,
    RootContainerDr,
    GroupContainerDr,
    ExpandBlockComponent,
    ExpandAllButtonComponent,
    ExpandOneButtonComponent
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
    ExpandBlockComponent
  ]
})
export class TreeContainerModule { }
