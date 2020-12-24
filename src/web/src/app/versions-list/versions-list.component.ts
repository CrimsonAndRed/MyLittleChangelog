import { Component, OnInit } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/service/http.service';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { VersionsListService } from './versions-list.service';

@Component({
  selector: 'versions-list',
  templateUrl: './versions-list.component.html',
  styleUrls: ['./versions-list.component.scss']
})
export class VersionsListComponent implements OnInit {

  versions: Version[];

  constructor(private http: Http, private route: ActivatedRoute, private preloaderService: PreloaderService, private versionsListService: VersionsListService) {
  }

  ngOnInit(): void {
    this.preloaderService.wrap(
      this.versionsListService.initVersions().pipe(
        tap((versions) => this.versions = versions),
      )
    );
  }

  gotoVersionPage(version: Version): string {
    return `version/${version.id}`;
  }

  onNewVersionCreated(obs: Observable<Version>): void {
    this.preloaderService.wrap(
      obs.pipe(
        tap((version) => this.versionsListService.createVersion(version)),
      )
    );
  }

  onVersionDelete(version: Version): void {
    this.preloaderService.wrap(
      this.http.delete(`http://localhost:8080/version/${version.id}`)
        .pipe(
          tap(() => this.versionsListService.deleteVersion(version)),
        )
    );
  }
}
