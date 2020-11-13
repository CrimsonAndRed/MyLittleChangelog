import { Component, OnInit, Type } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/http/http.service';
import { GroupContent, Group } from 'app/model/group-content';
import { GroupHeader, GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext, LeafHeader } from 'app/groups-sec/groups-sec.model';
import { GroupHeaderComponent } from './group-content/group-header/group-header.component';
import { LeafHeaderComponent } from './leaf-content/leaf-header/leaf-header.component';
import { tap } from 'rxjs/operators';
import { WholeVersionService } from 'app/service/whole-version.service';
import { SpinnerService } from 'app/spinner/spinner.service';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  config: GroupsSecConfig = new GroupSecConfigBuilder()
    .setGroupHeader(GroupHeaderComponent)
    .setLeafHeader(LeafHeaderComponent)
    .build();

  context: GroupsSecContext = {
    allGroups: null
  };

  constructor(private http: Http,
              private route: ActivatedRoute,
              public wholeVersionService: WholeVersionService,
              private spinnerService: SpinnerService) {
  }

  ngOnInit(): void {
    this.spinnerService.wrapSpinner(
      this.refresh()
    );
  }

  handlePreviousNodeChosen(obs: Observable<void>) {
    this.spinnerService.wrapSpinner(
      obs.pipe(
        switchMap(() => this.refresh())
      )
    );
  }

  refresh(): Observable<WholeVersion> {
    return this.wholeVersionService.initWholeVersion(this.route.snapshot.params.id)
      .pipe(
        tap((res) => this.context.allGroups = res.groupContent)
      )
  }
}
