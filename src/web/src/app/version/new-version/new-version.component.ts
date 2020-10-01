import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Http } from 'app/http/http.service';
import { Version } from 'app/model/version';

@Component({
  selector: 'new-version',
  templateUrl: './new-version.component.html',
  styleUrls: ['./new-version.component.scss']
})
export class NewVersionComponent {

  @Output() onNewVersion = new EventEmitter<Version>();

  constructor(private http: Http) {
  }

  onNewVersionButtonClick(): void {
     this.http.post<Version>('http://localhost:8080/version')
        .subscribe(version => this.onNewVersion.emit(version));
  }

}
