import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MatSelectModule } from '@angular/material/select';

import { GroupsSecComponent } from './groups-sec.component';
import { GroupLeavesSecComponent } from './group-leaves-sec/group-leaves-sec.component';
import { LeafContentSecComponent } from './leaf-content-sec/leaf-content-sec.component';
import { GroupContentSecComponent } from './group-content-sec/group-content-sec.component';
import { GlobalHeaderDr } from './groups-sec.directive';
import { GroupHeaderDr } from './groups-sec.directive';
import { LeafHeaderDr } from './groups-sec.directive';
import { ExpandOneButtonComponent } from './expand-block/expand-one-button/expand-one-button.component';
import { ExpandAllButtonComponent } from './expand-block/expand-all-button/expand-all-button.component';
import { ExpandBlockComponent } from './expand-block/expand-block.component';

@NgModule({
  declarations: [
    GroupsSecComponent,
    GroupLeavesSecComponent,
    LeafContentSecComponent,
    GroupContentSecComponent,
    ExpandBlockComponent,
    ExpandOneButtonComponent,
    ExpandAllButtonComponent,
    GlobalHeaderDr,
    GroupHeaderDr,
    LeafHeaderDr,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatSelectModule,
  ],
  exports: [
    GroupsSecComponent,
    GlobalHeaderDr
  ]
})
export class GroupsSecModule { }
