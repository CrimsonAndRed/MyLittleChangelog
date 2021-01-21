import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { DifferenceComponent } from './difference.component';
import { TreeContainerModule } from 'app/tree-view/tree-container.module';
import { DifferenceNodeComponent } from './difference-node/difference-node.component';

@NgModule({
  declarations: [
    DifferenceComponent,
    DifferenceNodeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatDialogModule,
    TreeContainerModule
  ],
})
export class DifferenceModule { }
