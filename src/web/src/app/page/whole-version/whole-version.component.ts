import { Component, OnInit } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router';
import { GroupSecConfigBuilder, GroupsSecConfig, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { GroupHeaderComponent } from './group-content/group-header/group-header.component';
import { LeafHeaderComponent } from './leaf-content/leaf-header/leaf-header.component';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { PreviousVersionGroupHeaderComponent } from './group-content/previous-version-group-header/previous-version-group-header.component';
import { PreviousVersionLeafHeaderComponent } from './leaf-content/previous-version-leaf-header/previous-version-leaf-header.component';
import { VersionHeaderComponent } from './version-header/version-header.component';
import { PreviousVersionVersionHeaderComponent } from './version-header/previous-version-version-header/previous-version-version-header.component';
import { GroupContent } from 'app/model/group-content';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  config: GroupsSecConfig;

  expandMap: Map<number, boolean> = new Map();

  context: GroupsSecContext = {
    allGroups: null
  };

  constructor(private route: ActivatedRoute,
              public wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService) {
    // TODO некоторый хак?
    this.wholeVersionService.wholeVersionSubject.subscribe({
      next: (v) => {
        this.config = this.recreateConfig(v);
        this.context.allGroups = v.groupContent;
        this.context.previousNodeChosen = this.handlePreviousNodeChosen.bind(this);
      }
    });
  }

  ngOnInit(): void {
    this.preloaderService.wrap(
      this.refresh()
    );
  }

  handlePreviousNodeChosen(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.refresh())
      )
    );
  }

  refresh(): Observable<WholeVersion> {
    return this.wholeVersionService.initWholeVersion(this.route.snapshot.params.id)
      .pipe(
        tap((v: WholeVersion) => this.config = this.recreateConfig(v)),
        tap((v: WholeVersion) => this.context.allGroups = v.groupContent),
        tap(() => this.context.previousNodeChosen = this.handlePreviousNodeChosen.bind(this)),
      );
  }

  private recreateConfig(v: WholeVersion): GroupsSecConfig {
    let newExpandMap: Map<number, boolean> = new Map();
    newExpandMap.set(null, this.expandMap.get(null) === true);
    this.recreateExpandMap(v.groupContent, newExpandMap);
    this.expandMap = newExpandMap;
    return new GroupSecConfigBuilder()
      .setGlobalHeader(v.canChange ? VersionHeaderComponent : PreviousVersionVersionHeaderComponent)
      .setGroupHeader(v.canChange ? GroupHeaderComponent : PreviousVersionGroupHeaderComponent)
      .setLeafHeader(v.canChange ? LeafHeaderComponent : PreviousVersionLeafHeaderComponent)
      .setExpandMap(this.expandMap)
      .build();
  }

  private recreateExpandMap(groups: GroupContent[], map: Map<number, boolean>) {
    groups.forEach(g => {
      map.set(g.vid, this.expandMap.get(g.vid) === true);
      this.recreateExpandMap(g.groupContent, map);
    });
  }
}
