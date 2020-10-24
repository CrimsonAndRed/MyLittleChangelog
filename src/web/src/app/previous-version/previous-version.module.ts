import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { GroupsSecModule } from 'app/groups-sec/groups-sec.module';
import { PreviousVersionSelectButtonComponent } from './previous-version-select-button/previous-version-select-button.component';
import { PreviousVersionModalComponent } from './previous-version-modal/previous-version-modal.component';
import { BaseHeaderComponent } from './header/base-header/base-header.component';
import { GroupHeaderComponent } from './header/group-header/group-header.component';
import { LeafHeaderComponent } from './header/leaf-header/leaf-header.component';

@NgModule({
  declarations: [
    PreviousVersionSelectButtonComponent,
    PreviousVersionModalComponent,
    BaseHeaderComponent,
    GroupHeaderComponent,
    LeafHeaderComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule,
    HttpClientModule,
    MatDialogModule,
    GroupsSecModule,
  ],
  exports: [
    PreviousVersionSelectButtonComponent,
  ]
})
export class PreviousVersionModule { }
