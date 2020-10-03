import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { WholeVersionComponent } from './whole-version.component';
import { GroupContentComponent } from './group-content/group-content.component';
import { LeafContentComponent } from './leaf-content/leaf-content.component';
import { NewGroupButtonComponent } from './new-group-button/new-group-button.component';
import { NewLeafButtonComponent } from './new-leaf-button/new-leaf-button.component';
import { NewGroupModalComponent } from './new-group-button/new-group-modal/new-group-modal.component';
import { MatDialogModule } from '@angular/material/dialog';

import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    WholeVersionComponent,
    GroupContentComponent,
    LeafContentComponent,
    NewGroupButtonComponent,
    NewLeafButtonComponent,
    NewGroupModalComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatDialogModule,
  ],
})
export class WholeVersionModule { }
