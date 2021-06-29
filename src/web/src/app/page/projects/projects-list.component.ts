import { Component, OnInit } from '@angular/core';
import { Version } from 'app/model/version';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { ProjectsListService } from './projects-list.service';
import { Project } from 'app/model/project';

@Component({
  selector: 'projects-list',
  templateUrl: './projects-list.component.html',
  styleUrls: ['./projects-list.component.scss']
})
export class ProjectsListComponent implements OnInit {

  projects: Project[] = [];

  constructor(private preloaderService: PreloaderService,
              private projectsListService: ProjectsListService) {
  }

  ngOnInit(): void {
    this.preloaderService.wrap(
      this.projectsListService.initProjects().pipe(
        tap((projects) => this.projects = projects),
      )
    );
  }

  gotoProjectPage(project: Project): string {
    return `/project/${project.id}`;
  }

  onProjectDelete(project: Project): void {
    this.projectsListService.deleteProject(project)
  }
}
