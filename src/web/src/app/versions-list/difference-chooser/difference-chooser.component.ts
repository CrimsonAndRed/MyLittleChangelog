import { Component, OnInit, Input } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/http/http.service';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'difference-chooser',
  templateUrl: './difference-chooser.component.html',
  styleUrls: ['./difference-chooser.component.scss']
})
export class DifferenceChooserComponent  {

  @Input() versions: Version[]
  fromVersion: Version;
  toVersion: Version;

  constructor() {
  }

  onToVersionChoose(version: Version) {
    this.toVersion = version;
  }

  onFromVersionChoose(version: Version) {
    this.fromVersion = version;
  }

  onGoToDifference() {

  }
}
