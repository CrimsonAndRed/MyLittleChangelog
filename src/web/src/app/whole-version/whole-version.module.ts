import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { GroupsSecModule } from 'app/groups-sec/groups-sec.module';
import { VersionHeaderComponent } from './version-header/version-header.component';
import { PreviousVersionModule } from 'app/previous-version/previous-version.module';
import { WholeVersionComponent } from './whole-version.component';
import { GroupHeaderComponent } from './group-content/group-header/group-header.component';
import { DeleteGroupButtonComponent } from './group-content/buttons/delete-group-button/delete-group-button.component';
import { MaterializeGroupButtonComponent } from './group-content/buttons/materialize-group-button/materialize-group-button.component';
import { DematerializeGroupButtonComponent } from './group-content/buttons/dematerialize-group-button/dematerialize-group-button.component';
import { EditGroupButtonComponent } from './group-content/buttons/edit-group-button/edit-group-button.component';
import { LeafHeaderComponent } from './leaf-content/leaf-header/leaf-header.component';
import { DeleteLeafButtonComponent } from './leaf-content/buttons/delete-leaf-button/delete-leaf-button.component';
import { EditLeafButtonComponent } from './leaf-content/buttons/edit-leaf-button/edit-leaf-button.component';
import { MatDialogModule } from '@angular/material/dialog';
import { NewGroupButtonComponent } from './group-content/buttons/new-group-button/new-group-button.component';
import { NewLeafButtonComponent } from './leaf-content/buttons/new-leaf-button/new-leaf-button.component';
import { EditGroupModalComponent } from './group-content/buttons/edit-group-button/edit-group-modal/edit-group-modal.component';
import { NewGroupModalComponent } from './group-content/buttons/new-group-button/new-group-modal/new-group-modal.component';
import { NewLeafModalComponent } from './leaf-content/buttons/new-leaf-button/new-leaf-modal/new-leaf-modal.component';
import { EditLeafModalComponent } from './leaf-content/buttons/edit-leaf-button/edit-leaf-modal/edit-leaf-modal.component';

@NgModule({
  declarations: [
    WholeVersionComponent,

    VersionHeaderComponent,

    GroupHeaderComponent,
    NewGroupButtonComponent,
    NewGroupModalComponent,
    DeleteGroupButtonComponent,
    MaterializeGroupButtonComponent,
    DematerializeGroupButtonComponent,
    EditGroupButtonComponent,
    EditGroupModalComponent,

    LeafHeaderComponent,
    NewLeafButtonComponent,
    NewLeafModalComponent,
    DeleteLeafButtonComponent,
    EditLeafButtonComponent,
    EditLeafModalComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatDialogModule,
    GroupsSecModule,
    PreviousVersionModule,
  ],
})
export class WholeVersionModule { }
