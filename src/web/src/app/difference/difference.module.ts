import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { GroupsSecModule } from 'app/groups-sec/groups-sec.module';
import { MatDialogModule } from '@angular/material/dialog';
import { DifferenceComponent } from './difference.component';
import { GroupsThirdComponent } from './groups-third/groups-third.component';
import { GroupThirdComponent } from './groups-third/group-third/group-third.component';
import { GroupsLeavesThirdComponent } from './groups-third/groups-leaves-third/groups-leaves-third.component';
import { LeafThirdComponent } from './groups-third/leaf-third/leaf-third.component';

@NgModule({
  declarations: [
    DifferenceComponent,
    GroupThirdComponent,
    GroupsLeavesThirdComponent,
    LeafThirdComponent,
    GroupsThirdComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatDialogModule,
  ],
})
export class DifferenceModule { }
