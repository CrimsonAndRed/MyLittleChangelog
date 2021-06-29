import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ProjectsListService } from '../projects-list.service';
import { NewProjectModalComponent } from './new-project-modal/new-project-modal.component';

@Component({
  selector: 'new-project-button',
  templateUrl: './new-project-button.component.html',
  styleUrls: ['./new-project-button.component.scss']
})
export class NewProjectButtonComponent {

  constructor(private projectsListService: ProjectsListService,
              private dialog: MatDialog) {
  }

  onNewProjectButtonClick(): void {

    const dialogRef = this.dialog.open(NewProjectModalComponent, {
      hasBackdrop: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.projectsListService.createProject(result)
      }
    });
  }
}
