import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Http } from 'app/service/http.service';
import { Version } from 'app/model/version';
import { Observable } from 'rxjs';
import { VersionsListService } from '../versions-list.service';
import { PreloaderService } from 'app/preloader/preloader.service';

@Component({
  selector: 'new-version-button',
  templateUrl: './new-version-button.component.html',
  styleUrls: ['./new-version-button.component.scss']
})
export class NewVersionButtonComponent {

  constructor(private versionsListService: VersionsListService,
              private preloaderService: PreloaderService) {
  }

  onNewVersionButtonClick(): void {
    this.preloaderService.wrap(
      this.versionsListService.createVersion()
    );
  }
}
