import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { ProjectsListComponent } from './projects-list.component';
import { NewProjectButtonComponent } from './new-project-button/new-project-button.component';

import { HttpClientModule } from '@angular/common/http';
import { MatSelectModule } from '@angular/material/select';
import { NewProjectModalComponent } from './new-project-button/new-project-modal/new-project-modal.component';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    ProjectsListComponent,
    NewProjectButtonComponent,
    NewProjectModalComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatSelectModule,
    MatDialogModule
  ],
})
export class ProjectsListModule { }
