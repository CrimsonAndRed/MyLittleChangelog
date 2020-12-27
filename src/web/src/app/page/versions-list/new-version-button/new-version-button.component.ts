import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Http } from 'app/service/http.service';
import { Version } from 'app/model/version';
import { Observable } from 'rxjs';

@Component({
  selector: 'new-version-button',
  templateUrl: './new-version-button.component.html',
  styleUrls: ['./new-version-button.component.scss']
})
export class NewVersionButtonComponent {

  @Output() onNewVersion = new EventEmitter<Observable<Version>>();

  constructor(private http: Http) {
  }

  onNewVersionButtonClick(): void {
     this.onNewVersion.emit(this.http.post<Version>('http://localhost:8080/version'));
  }
}
