import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MatSelectModule } from '@angular/material/select';

import { GroupsSecComponent } from './groups-sec.component';
import { LeafContentSecComponent } from './leaf-content-sec/leaf-content-sec.component';
import { GroupContentSecComponent } from './group-content-sec/group-content-sec.component';
import { GroupLeavesSec } from './group-leaves-sec/group-leaves-sec.component';

@NgModule({
  declarations: [
    GroupsSecComponent,
    LeafContentSecComponent,
    GroupContentSecComponent,
    GroupLeavesSec,
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
  ]
})
export class GroupsSecModule { }
