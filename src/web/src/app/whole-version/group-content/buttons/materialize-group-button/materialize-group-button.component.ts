import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';
import { GroupContent, Group, NewGroup } from 'app/model/group-content';

@Component({
  selector: 'materialize-group-button',
  templateUrl: './materialize-group-button.component.html',
  styleUrls: ['./materialize-group-button.component.scss']
})
export class MaterializeGroupButtonComponent {

  @Input() group: GroupContent;
  @Input() parentGroup: GroupContent;
  @Output() onMaterializeGroup = new EventEmitter<Group>();

  constructor(private http: Http, private route: ActivatedRoute) {}

  onMaterializeClick(): void {
    const versionId = this.route.snapshot.data.version.id;
    const groupId = this.group.id;

    const newGroup: NewGroup = {
      vid: this.group.vid,
      name: this.group.name,
      parentId: this.parentGroup?.id,
    }

    this.http.post<Group>(`http://localhost:8080/version/${versionId}/group`, newGroup)
        .subscribe((res) => this.onMaterializeGroup.emit(res));
  }
}
