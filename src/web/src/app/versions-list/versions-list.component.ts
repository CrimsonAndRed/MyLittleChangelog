import { Component, OnInit } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/http/http.service';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'versions-list',
  templateUrl: './versions-list.component.html',
  styleUrls: ['./versions-list.component.scss']
})
export class VersionsListComponent implements OnInit {

  versions: Version[];

  constructor(private http: Http, private route: ActivatedRoute, private preloaderService: PreloaderService) {
  }

  ngOnInit(): void {
    this.versions = this.route.snapshot.data.versions;
  }

  gotoVersionPage(version: Version): string {
    return `version/${version.id}`;
  }

  onNewVersionCreated(obs: Observable<Version>): void {
    this.preloaderService.wrap(
      obs.pipe(
        tap((version) => this.versions.push(version)),
      )
    );
  }

  onVersionDelete(version: Version): void {
    this.preloaderService.wrap(
      this.http.delete(`http://localhost:8080/version/${version.id}`)
        .pipe(
          tap(() => this.versions = this.versions.filter(v => v.id !== version.id)),
        )
    );
  }
}
