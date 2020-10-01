import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { WholeVersionComponent } from './whole-version.component';
import { GroupContentComponent } from './group-content/group-content.component';
import { LeafContentComponent } from './leaf-content/leaf-content.component';

import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    WholeVersionComponent,
    GroupContentComponent,
    LeafContentComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
  ],
})
export class WholeVersionModule { }
