import { Component, OnInit } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router';
import { GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupHeaderComponent } from './group-content/group-header/group-header.component';
import { LeafHeaderComponent } from './leaf-content/leaf-header/leaf-header.component';
import { tap } from 'rxjs/operators';
import { WholeVersionService } from 'app/service/whole-version.service';
import { SpinnerService } from 'app/spinner/spinner.service';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { PreviousVersionGroupHeaderComponent } from './group-content/previous-version-group-header/previous-version-group-header.component';
import { PreviousVersionLeafHeaderComponent } from './leaf-content/previous-version-leaf-header/previous-version-leaf-header.component';
import { VersionHeaderComponent } from './version-header/version-header.component';
import { PreviousVersionVersionHeaderComponent } from './previous-version-version-header/previous-version-version-header.component';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  config: GroupsSecConfig;
  context: GroupsSecContext = {
    allGroups: null
  };

  constructor(private route: ActivatedRoute,
              public wholeVersionService: WholeVersionService,
              private spinnerService: SpinnerService) {
  }

  ngOnInit(): void {
    this.spinnerService.wrapSpinner(
      this.refresh()
    );
  }

  handlePreviousNodeChosen(obs: Observable<void>): void {
    this.spinnerService.wrapSpinner(
      obs.pipe(
        switchMap(() => this.refresh())
      )
    );
  }

  refresh(): Observable<WholeVersion> {
    return this.wholeVersionService.initWholeVersion(this.route.snapshot.params.id)
      .pipe(
        tap((v: WholeVersion) => this.config = this.createConfig(v)),
        tap((v: WholeVersion) => this.context.allGroups = v.groupContent),
        tap(() => this.context.previousNodeChosen = this.handlePreviousNodeChosen.bind(this)),
      );
  }

  private createConfig(v: WholeVersion): GroupsSecConfig {
    return new GroupSecConfigBuilder()
      .setGlobalHeader(v.canChange ? VersionHeaderComponent : PreviousVersionVersionHeaderComponent)
      .setGroupHeader(v.canChange ? GroupHeaderComponent : PreviousVersionGroupHeaderComponent)
      .setLeafHeader(v.canChange ? LeafHeaderComponent : PreviousVersionLeafHeaderComponent)
      .build();
  }
}
