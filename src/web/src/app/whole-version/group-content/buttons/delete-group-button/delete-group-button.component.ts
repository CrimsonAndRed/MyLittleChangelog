import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';

@Component({
  selector: 'delete-group-button',
  templateUrl: './delete-group-button.component.html',
  styleUrls: ['./delete-group-button.component.scss']
})
export class DeleteGroupButtonComponent {

  @Input() groupId: number;
  @Output() onDeleteGroup = new EventEmitter<void>();

  constructor(private http: Http, private route: ActivatedRoute) {}

  onDeleteClick(): void {
    const versionId = this.route.snapshot.data.version.id;
    const groupId = this.groupId;

    this.http.delete(`http://localhost:8080/version/${versionId}/group/${groupId}`)
        .subscribe(() => this.onDeleteGroup.emit());
  }
}
