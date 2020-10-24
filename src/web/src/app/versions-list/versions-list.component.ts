import { Component, OnInit } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/http/http.service';
import { RouterLink, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'versions-list',
  templateUrl: './versions-list.component.html',
  styleUrls: ['./versions-list.component.scss']
})
export class VersionsListComponent implements OnInit {

  versions: Version[];

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.versions = this.route.snapshot.data.versions;
  }

  gotoVersionPage(version: Version): string {
    return `version/${version.id}`;
  }

  onNewVersionCreated(version: Version): void {
    this.versions.push(version);
  }

  onVersionDelete(version: Version): void {
    this.http.delete(`http://localhost:8080/version/${version.id}`)
        .subscribe(() => this.versions = this.versions.filter(v => v.id !== version.id));
  }
}
