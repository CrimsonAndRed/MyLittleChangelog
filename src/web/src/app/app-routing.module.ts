import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VersionsListComponent } from './versions-list/versions-list.component';
import { WholeVersionComponent } from './whole-version/whole-version.component';
import { VersionsResolver } from './resolver/versions.resolver';
import { DifferenceComponent } from './difference/difference.component';
import { DifferenceResolver } from './resolver/difference.resolver';

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
  },
  {
    path: 'difference',
    component: DifferenceComponent,
    resolve: {
      difference: DifferenceResolver
    },
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
