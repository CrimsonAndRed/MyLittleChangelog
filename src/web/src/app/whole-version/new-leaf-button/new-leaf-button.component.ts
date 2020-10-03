import { Component, Input, Output } from '@angular/core';

import { GroupContent } from 'app/model/group-content';
import { Http } from 'app/http/http.service';
import { NewLeafWithId, NewLeaf} from 'app/model/new-leaf';
import { ActivatedRoute } from '@angular/router';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'new-leaf-button',
  templateUrl: './new-leaf-button.component.html',
  styleUrls: ['./new-leaf-button.component.scss']
})
export class NewLeafButtonComponent {

  @Output() onNewLeaf = new EventEmitter<NewLeafWithId>();

  @Input() groupContent: GroupContent;

  constructor(private http: Http, private route: ActivatedRoute) {
  }

  onNewLeafButtonClick() {
    const versionId = this.route.snapshot.data.version.id;
    const groupId = this.groupContent === null ? null : this.groupContent.id;

    const newLeaf: NewLeaf = {
      name: "22",
      valueType: 1,
      value: "11",
    }
    this.http.post<NewLeafWithId>(`http://localhost:8080/version/${versionId}/group/${groupId}/leaf`, newLeaf)
      .subscribe(newLeaf =>this.onNewLeaf.emit(newLeaf));
  }

}
