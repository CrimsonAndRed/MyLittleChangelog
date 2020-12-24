import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/whole-version/whole-version.service';

@Component({
  selector: 'dematerialize-group-button',
  templateUrl: './dematerialize-group-button.component.html',
  styleUrls: ['./dematerialize-group-button.component.scss']
})
export class DematerializeGroupButtonComponent {

  @Input() group: GroupContent;
  @Output() onDematerializeGroup = new EventEmitter<Observable<void>>();

  constructor(private http: Http, private route: ActivatedRoute, private wholeVersionService: WholeVersionService) {}

  onDematerializeClick(): void {
    const versionId = this.wholeVersionService.wholeVersion.id;
    const groupId = this.group.id;
    const params = new HttpParams().set('hierarchy', 'false');

    this.onDematerializeGroup.emit(this.http.delete<void>(`http://localhost:8080/version/${versionId}/group/${groupId}`, params));
  }
}
