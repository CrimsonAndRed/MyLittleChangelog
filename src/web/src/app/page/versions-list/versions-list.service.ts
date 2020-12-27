import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { Version } from 'app/model/version';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root',
})
export class VersionsListService {
  public versions: Version[] = [];

  constructor(private http: Http, private preloaderService: PreloaderService) {  }

  initVersions(): Observable<Version[]> {
    return this.http.get<Version[]>('http://localhost:8080/version')
      .pipe(
        tap(res => this.versions = res),
      );
  }

  createVersion(version: Version) {
    this.versions.push(version)
  }

  deleteVersion(version: Version) {
    let index = this.versions.map((v) => v.id).indexOf(version.id)
    this.versions.splice(index)
  }
}
