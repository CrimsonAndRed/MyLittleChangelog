import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VersionsListComponent } from './versions-list/versions-list.component';
import { WholeVersionComponent } from './whole-version/whole-version.component';
import { VersionsResolver } from './resolver/versions.resolver';
import { WholeVersionResolver } from './resolver/whole-version.resolver';

const routes: Routes = [
  {
    path: '',
    component: VersionsListComponent,
    resolve: {
      versions: VersionsResolver
    },
  },
  {
    path: 'version/:id',
    component: WholeVersionComponent,
    resolve: {
      version: WholeVersionResolver
    }
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
