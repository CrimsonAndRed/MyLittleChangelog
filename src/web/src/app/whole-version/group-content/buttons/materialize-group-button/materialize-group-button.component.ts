import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';
import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/whole-version/whole-version.service';

@Component({
  selector: 'materialize-group-button',
  templateUrl: './materialize-group-button.component.html',
  styleUrls: ['./materialize-group-button.component.scss']
})
export class MaterializeGroupButtonComponent {

  @Input() group: GroupContent;
  @Input() parentGroup: GroupContent;
  @Output() onMaterializeGroup = new EventEmitter<Observable<Group>>();

  constructor(private http: Http, private route: ActivatedRoute, private wholeVersionService: WholeVersionService) {}

  onMaterializeClick(): void {
    const versionId = this.wholeVersionService.wholeVersion.id;

    const newGroup: NewGroup = {
      vid: this.group.vid,
      name: this.group.name,
      parentVid: this.parentGroup?.vid,
    };
    this.onMaterializeGroup.emit(this.http.post<Group>(`http://localhost:8080/version/${versionId}/group`, newGroup));
  }
}
