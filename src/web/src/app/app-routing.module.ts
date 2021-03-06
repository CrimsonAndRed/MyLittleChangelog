import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VersionsListComponent } from './page/versions-list/versions-list.component';
import { WholeVersionComponent } from './page/whole-version/whole-version.component';
import { DifferenceComponent } from './page/difference/difference.component';
import { LoginComponent } from './page/login/login.component';
import { ProjectsListComponent } from './page/projects/projects-list.component';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'project/:id',
    component: VersionsListComponent,
  },
  {
    path: 'projects',
    component: ProjectsListComponent,
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
