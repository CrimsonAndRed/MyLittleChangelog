import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'delete-leaf-button',
  templateUrl: './delete-leaf-button.component.html',
  styleUrls: ['./delete-leaf-button.component.scss']
})
export class DeleteLeafButtonComponent {

  @Input() groupId: number;
  @Input() leafId: number;
  @Output() onDeleteLeaf = new EventEmitter<Observable<void>>();

  constructor(private http: Http, private route: ActivatedRoute) {}

  onDeleteClick(): void {
    const versionId = this.route.snapshot.params.id;
    const groupId = this.groupId;
    const leafId = this.leafId;

    this.onDeleteLeaf.emit(this.http.delete(`http://localhost:8080/version/${versionId}/group/${groupId}/leaf/${leafId}`))
  }

}
