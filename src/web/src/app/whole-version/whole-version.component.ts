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
    this.version.groupContent = [{
      id: 1,
      vid: 1,
      name: 'Group content name',
      groupContent: [
        {
          id: 2,
          vid: 2,
          name: 'Group content name 2',
          groupContent: [],
          leafContent: [],
        }
      ],
      leafContent: [
        {
          id: 3,
          vid: 3,
          name: 'Leaf Content name 3',
          valueType: 0,
          value: 'Leaf Content Value 3',
        }
      ],
    }];
  }
}
