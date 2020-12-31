import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { Version } from 'app/model/version';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { OperatorFunction } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class VersionsListService {
  public versions: Version[] = [];

  constructor(private http: Http,
              private preloaderService: PreloaderService) {  }

  initVersions(): Observable<Version[]> {
    return this.http.get<Version[]>('http://localhost:8080/version')
      .pipe(
        tap(res => this.versions = res),
      );
  }

  createVersion(cb: OperatorFunction<Version, Version> = tap()) {
    this.preloaderService.wrap(
      this.http.post<Version>('http://localhost:8080/version')
        .pipe(
          cb,
          tap((version) => this.versions.push(version))
        )
      );
  }

  deleteVersion(version: Version, cb: OperatorFunction<void, void> = tap()) {
    this.preloaderService.wrap(
      this.http.delete(`http://localhost:8080/version/${version.id}`)
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
