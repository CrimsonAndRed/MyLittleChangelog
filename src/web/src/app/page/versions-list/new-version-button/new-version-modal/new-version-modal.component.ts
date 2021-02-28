import { Component } from '@angular/core';
import { NewVersion } from 'app/model/version';

@Component({
  selector: 'new-version-modal',
  templateUrl: './new-version-modal.component.html',
  styleUrls: ['./new-version-modal.component.scss']
})
export class NewVersionModalComponent  {

  version: NewVersion = {
    name: null
  };
}
