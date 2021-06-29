import { Component } from '@angular/core';
import { NewProject } from 'app/model/project';

@Component({
  selector: 'new-project-modal',
  templateUrl: './new-project-modal.component.html',
  styleUrls: ['./new-project-modal.component.scss']
})
export class NewProjectModalComponent  {

  project: NewProject = {
    name: null,
    description: null
  };
}
