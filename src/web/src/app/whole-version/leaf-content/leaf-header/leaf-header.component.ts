import { Component, OnInit } from '@angular/core';
import { LeafHeader, LeafHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { WholeVersionService } from 'app/whole-version/whole-version.service';
import { Observable } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/http/http.service';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'leaf-header',
  templateUrl: './leaf-header.component.html',
  styleUrls: ['./leaf-header.component.scss']
})
export class LeafHeaderComponent implements LeafHeader, OnInit {

  data: LeafHeaderData;
  ctx: GroupsSecContext;

  constructor(private http: Http,
              private route: ActivatedRoute,
              private wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService) {}

  ngOnInit(): void {
  }

  handleDeleteLeaf(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.wholeVersionService.deleteLeaf())
      )
    );
  }

  handleUpdateLeaf(obs: Observable<void>): void {
    this.preloaderService.wrap(
      obs.pipe(
        switchMap(() => this.wholeVersionService.updateLeaf())
      )
    );
  }

  isUpButtonShowed(): boolean {
    return this.data.parentGroup.leafContent[0].id !== this.data.leaf.id;
  }

  isDownButtonShowed(): boolean {
    return this.data.parentGroup.leafContent[this.data.parentGroup.leafContent.length - 1].id !== this.data.leaf.id;
  }

  handleMoveUp(): void {
    this.moveLeaf(this.data.parentGroup.leafContent[this.findIdxInParent() - 1].id);
  }

  handleMoveDown(): void {
    this.moveLeaf(this.data.parentGroup.leafContent[this.findIdxInParent() + 1].id);
  }

  private findIdxInParent(): number {
    return this.data.parentGroup.leafContent.findIndex(l => l.id === this.data.leaf.id);
  }

  private moveLeaf(changeAgainstId: number): void {
    const versionId = this.route.snapshot.params.id;
    const parentId = this.data.parentGroup.id;
    const leafId = this.data.leaf.id;

    const dto = { changeAgainstId };

    this.preloaderService.wrap(
      this.http.patch(`http://localhost:8080/version/${versionId}/group/${parentId}/leaf/${leafId}/position`, dto)
        .pipe(
          tap(() => this.wholeVersionService.swapLeaves(this.data.parentGroup.vid, leafId, changeAgainstId))
        )
    );
  }
}
