import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { NewVersion, Version } from 'app/model/version';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { OperatorFunction } from 'rxjs';
import { environment } from 'environments/environment';


@Injectable({
  providedIn: 'root',
})
export class VersionsListService {
  public versions: Version[] = [];
  public projectId: number;

  constructor(private http: Http,
              private preloaderService: PreloaderService) {  }

  initVersions(projectId: number): Observable<Version[]> {
    this.projectId = projectId;
    return this.http.get<Version[]>(`${environment.backendPath}/project/${this.projectId}/version`)
      .pipe(
        tap(res => this.versions = res),
      );
  }

  createVersion(version: NewVersion, cb: OperatorFunction<Version, Version> = tap()) {
    this.preloaderService.wrap(
      this.http.post<Version>(`${environment.backendPath}/project/${this.projectId}/version`, version)
        .pipe(
          cb,
          tap((version) => this.versions.push(version))
        )
      );
  }

  deleteVersion(version: Version, cb: OperatorFunction<void, void> = tap()) {
    this.preloaderService.wrap(
      this.http.delete(`${environment.backendPath}/version/${version.id}`)
        .pipe(
          cb,
          tap(() => {
            let index = this.versions.map((v) => v.id).indexOf(version.id)
            this.versions.splice(index)
          }),
        )
      );
  }
}
