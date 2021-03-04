import { Component, OnInit } from '@angular/core';
import { Version } from 'app/model/version';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { VersionsListService } from './versions-list.service';

@Component({
  selector: 'versions-list',
  templateUrl: './versions-list.component.html',
  styleUrls: ['./versions-list.component.scss']
})
export class VersionsListComponent implements OnInit {

  versions: Version[] = [];

  constructor(private preloaderService: PreloaderService,
              private versionsListService: VersionsListService) {
  }

  ngOnInit(): void {
    this.preloaderService.wrap(
      this.versionsListService.initVersions().pipe(
        tap((versions) => this.versions = versions),
      )
    );
  }

  gotoVersionPage(version: Version): string {
    return `/version/${version.id}`;
  }

  onVersionDelete(version: Version): void {
    this.versionsListService.deleteVersion(version)
  }
}
