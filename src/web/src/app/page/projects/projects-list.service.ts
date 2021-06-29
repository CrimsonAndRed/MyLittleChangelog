import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { OperatorFunction } from 'rxjs';
import { environment } from 'environments/environment';
import { NewProject, Project } from 'app/model/project';


@Injectable({
  providedIn: 'root',
})
export class ProjectsListService {
  public projects: Project[] = [];

  constructor(private http: Http,
              private preloaderService: PreloaderService) {  }

  initProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`${environment.backendPath}/project`)
      .pipe(
        tap(res => this.projects = res),
      );
  }

  createProject(version: NewProject, cb: OperatorFunction<Project, Project> = tap()) {
    this.preloaderService.wrap(
      this.http.post<Project>(`${environment.backendPath}/project`, version)
        .pipe(
          cb,
          tap((project) => this.projects.push(project))
        )
      );
  }

  deleteProject(project: Project, cb: OperatorFunction<void, void> = tap()) {
    this.preloaderService.wrap(
      this.http.delete(`${environment.backendPath}/project/${project.id}`)
        .pipe(
          cb,
          tap(() => {
            let index = this.projects.map((v) => v.id).indexOf(project.id)
            this.projects.splice(index)
          }),
        )
      );
  }
}
