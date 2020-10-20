import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { WholeVersionComponent } from './whole-version.component';
import { GroupContentComponent } from './group-content/group-content.component';
import { LeafContentComponent } from './leaf-content/leaf-content.component';
import { GroupsLeavesComponent } from './groups-leaves/groups-leaves.component';
import { NewGroupButtonComponent } from './groups-leaves/new-group-button/new-group-button.component';
import { NewLeafButtonComponent } from './groups-leaves/new-leaf-button/new-leaf-button.component';
import { NewGroupModalComponent } from './group-content/new-group-modal/new-group-modal.component';
import { NewLeafModalComponent } from './leaf-content/new-leaf-modal/new-leaf-modal.component';
import { EditLeafModalComponent } from './leaf-content/edit-leaf-modal/edit-leaf-modal.component';
import { EditGroupModalComponent } from './group-content/edit-group-modal/edit-group-modal.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { FromPastGroupsButton } from './past-groups/from-past-groups-button/from-past-groups-button.component';
import { FromPastGroupsModal } from './past-groups/from-past-groups-modal/from-past-groups-modal.component';
import { PastGroup } from './past-groups/from-past-groups-modal/past-group/past-group.component';
import { PastLeaf } from './past-groups/from-past-groups-modal/past-leaf/past-leaf.component';

import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    WholeVersionComponent,
    GroupContentComponent,
    LeafContentComponent,
    GroupsLeavesComponent,
    NewGroupButtonComponent,
    NewLeafButtonComponent,
    NewGroupModalComponent,
    NewLeafModalComponent,
    EditLeafModalComponent,
    EditGroupModalComponent,
    FromPastGroupsModal,
    FromPastGroupsButton,
    PastGroup,
    PastLeaf,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    MatDialogModule,
    MatSelectModule,
  ],
})
export class WholeVersionModule { }
