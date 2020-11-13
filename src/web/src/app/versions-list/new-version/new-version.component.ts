import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Http } from 'app/http/http.service';
import { Version } from 'app/model/version';
import { Observable } from 'rxjs';

@Component({
  selector: 'new-version',
  templateUrl: './new-version.component.html',
  styleUrls: ['./new-version.component.scss']
})
export class NewVersionComponent {

  @Output() onNewVersion = new EventEmitter<Observable<Version>>();

  constructor(private http: Http) {
  }

  onNewVersionButtonClick(): void {
     this.onNewVersion.emit(this.http.post<Version>('http://localhost:8080/version'));
  }
}
