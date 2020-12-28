import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { GroupsSecModule } from 'app/groups-sec/groups-sec.module';
import { VersionHeaderComponent } from './version-header/version-header.component';
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
import { GroupMovementNodeComponent } from './common/tree-node/group-movement-node/group-movement-node.component';
import { PreviousVersionVersionHeaderComponent } from './version-header/previous-version-version-header/previous-version-version-header.component';
import { MatSelectModule } from '@angular/material/select';
import { MoveUpDownButtonComponent } from './common/move-updown-button/move-updown-button.component';
import { TreeContainerModule } from 'app/tree-view/tree-container.module';
import { WholeVersionNodeComponent } from './common/tree-node/whole-version-node/whole-version-node.component';
import { GroupBodyComponent } from './group-content/group-body/group-body.component';
import { LeafBodyComponent } from './leaf-content/leaf-body/leaf-body.component';
import { PreviousVersionNodeComponent } from './common/tree-node/previus-version-node/previous-version-node.component';
import { PreviousVersionSelectButtonComponent } from './version-header/previous-version/previous-version-select-button/previous-version-select-button.component';
import { PreviousVersionModalComponent } from './version-header/previous-version/previous-version-modal/previous-version-modal.component';

@NgModule({
  declarations: [
    WholeVersionComponent,

    VersionHeaderComponent,
    PreviousVersionVersionHeaderComponent,

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

    GroupMovementNodeComponent,
    MoveUpDownButtonComponent,
    GroupBodyComponent,
    LeafBodyComponent,

    WholeVersionNodeComponent,
    PreviousVersionSelectButtonComponent,
    PreviousVersionModalComponent,
    PreviousVersionNodeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatDialogModule,
    GroupsSecModule,
    MatSelectModule,
    TreeContainerModule
  ],
})
export class WholeVersionModule { }
