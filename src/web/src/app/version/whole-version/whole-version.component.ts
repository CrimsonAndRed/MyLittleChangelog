import { Component, OnInit } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router'
import { Http } from 'app/http/http.service'

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  version: WholeVersion;

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.version = this.route.snapshot.data.version;
    //const versionId = this.router.snapshot.paramMap.get('id');
    //this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
    //  .subscribe(model => this.version = model);
  }
}
