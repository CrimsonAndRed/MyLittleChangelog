import { Component, OnInit } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/http/http.service';
import { RouterLink, ActivatedRoute } from '@angular/router'

@Component({
  selector: 'versions-list',
  templateUrl: './versions-list.component.html',
  styleUrls: ['./versions-list.component.scss']
})
export class VersionsListComponent implements OnInit {

  versions: Version[];

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.versions = this.route.snapshot.data.versions;
    //this.http.get<Version[]>('http://localhost:8080/version')
    // .subscribe(model => this.versions = model)
  }

  gotoVersionPage(version: Version): string {
    return `version/${version.id}`;
  }
}
