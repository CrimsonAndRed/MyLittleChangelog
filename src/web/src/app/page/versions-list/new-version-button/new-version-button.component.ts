import { Component } from '@angular/core';
import { VersionsListService } from '../versions-list.service';

@Component({
  selector: 'new-version-button',
  templateUrl: './new-version-button.component.html',
  styleUrls: ['./new-version-button.component.scss']
})
export class NewVersionButtonComponent {

  constructor(private versionsListService: VersionsListService) {
  }

  onNewVersionButtonClick(): void {
    this.versionsListService.createVersion()
  }
}
