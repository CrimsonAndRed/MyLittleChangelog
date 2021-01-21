import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VersionsListComponent } from './page/versions-list/versions-list.component';
import { WholeVersionComponent } from './page/whole-version/whole-version.component';
import { DifferenceComponent } from './page/difference/difference.component';

const routes: Routes = [
  {
    path: '',
    component: VersionsListComponent,
  },
  {
    path: 'version/:id',
    component: WholeVersionComponent,
  },
  {
    path: 'difference',
    component: DifferenceComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
