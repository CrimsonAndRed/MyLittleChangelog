import { Component, Input, Output } from '@angular/core';

import { GroupContent } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { NewGroupWithId, NewGroup} from 'app/model/new-group';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'new-group-button',
  templateUrl: './new-group-button.component.html',
  styleUrls: ['./new-group-button.component.scss']
})
export class NewGroupButtonComponent {

  @Output() onNewGroup = new EventEmitter<NewGroupWithId>();

  @Input() groupContent: GroupContent;

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  onNewGroupButtonClick() {
    const versionId = this.route.snapshot.data.version.id;
    const parentId = this.groupContent === null ? null : this.groupContent.id;

    const newGroup: NewGroup = {
      vid: null,
      parentId: parentId,
      name: "11",
    }
    this.http.post<NewGroupWithId>(`http://localhost:8080/version/${versionId}/group`, newGroup)
      .subscribe(newGroup =>this.onNewGroup.emit(newGroup) );
  }

}
