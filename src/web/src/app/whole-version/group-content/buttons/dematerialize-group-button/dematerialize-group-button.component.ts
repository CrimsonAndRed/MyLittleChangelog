import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';
import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'dematerialize-group-button',
  templateUrl: './dematerialize-group-button.component.html',
  styleUrls: ['./dematerialize-group-button.component.scss']
})
export class DematerializeGroupButtonComponent {

  @Input() group: GroupContent;
  @Output() onDematerializeGroup = new EventEmitter<Group>();

  constructor(private http: Http, private route: ActivatedRoute) {}

  onDematerializeClick(): void {
    const versionId = this.route.snapshot.params.id;
    const groupId = this.group.id;
    const params = new HttpParams().set('hierarchy', 'false');

    this.http.delete<Group>(`http://localhost:8080/version/${versionId}/group/${groupId}`, params)
        .subscribe((res) => this.onDematerializeGroup.emit(res));
  }
}
