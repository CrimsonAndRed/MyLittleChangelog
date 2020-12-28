import { Component, OnInit } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { ActivatedRoute } from '@angular/router';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';

@Component({
  selector: 'whole-version',
  templateUrl: './whole-version.component.html',
  styleUrls: ['./whole-version.component.scss']
})
export class WholeVersionComponent implements OnInit {

  expandMap: Map<number, boolean> = new Map();

  constructor(private route: ActivatedRoute,
              public wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService) {
    // TODO некоторый хак?
    this.wholeVersionService.wholeVersionSubject.subscribe({
      next: (v) => {
        // this.config = this.recreateConfig(v);
        // this.context.allGroups = v.groupContent;
        // this.context.previousNodeChosen = this.handlePreviousNodeChosen.bind(this);
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
        // tap((v: WholeVersion) => this.config = this.recreateConfig(v)),
        // tap((v: WholeVersion) => this.context.allGroups = v.groupContent),
        // tap(() => this.context.previousNodeChosen = this.handlePreviousNodeChosen.bind(this)),
      );
  }

  // private recreateExpandMap(groups: GroupContent[], map: Map<number, boolean>) {
  //   groups.forEach(g => {
  //     map.set(g.vid, this.expandMap.get(g.vid) === true);
  //     this.recreateExpandMap(g.groupContent, map);
  //   });
  // }
}
