import { Injectable } from '@angular/core';
import { Http } from 'app/http/http.service';
import { WholeVersion } from 'app/model/whole-version';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, UpdatedLeaf } from 'app/model/leaf-content';
import { PreloaderService } from '../preloader/preloader.service';

@Injectable({
  providedIn: 'root',
})
export class WholeVersionService {

  public wholeVersion: WholeVersion = null;

  private groupsByVid: Map<number, GroupContent> = new Map<number, GroupContent>();

  constructor(private http: Http, private preloaderService: PreloaderService) {  }

  initWholeVersion(versionId: number): Observable<WholeVersion> {
    this.wholeVersion = null;
    this.groupsByVid = new Map<number, GroupContent>();

    return this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
      .pipe(
        tap(res => this.wholeVersion = res),
        tap(res => res.groupContent.forEach(g => this.addGroupToMap(g))),
      );
  }

  addGroupToParent(group: GroupContent, parentVid: number): void {
    if (parentVid !== null) {
      this.groupsByVid.get(parentVid).groupContent.push(group);
    } else {
      this.wholeVersion.groupContent.push(group);
    }
    this.groupsByVid.set(group.vid, group);
  }

  addLeafToParent(leaf: LeafContent, parentVid: number): void {
    this.groupsByVid.get(parentVid).leafContent.push(leaf);
  }

  updateLeaf(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersion.id);
  }

  deleteLeaf(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersion.id);
  }

  deleteGroup(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersion.id);
  }

  updateGroup(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersion.id);
  }

  materializeGroup(group: Group): void {
    const updateGroup = this.groupsByVid.get(group.vid);

    updateGroup.name = group.name;
    updateGroup.vid = group.vid;
    updateGroup.id = group.id;
    updateGroup.realNode = true;
    updateGroup.isEarliest = false;
  }

  dematerializeGroup(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersion.id);
  }

  swapLeaves(parentVid: number, id1: number, id2: number): void {
    const group = this.groupsByVid.get(parentVid);
    const leafIdxFs = group.leafContent.findIndex(l => l.id === id1);
    const leafIdxSc = group.leafContent.findIndex(l => l.id === id2);
    const tmpLeaf = group.leafContent[leafIdxFs];
    group.leafContent[leafIdxFs] = group.leafContent[leafIdxSc];
    group.leafContent[leafIdxSc] = tmpLeaf;
  }

  private addGroupToMap(group: GroupContent): void {
    this.groupsByVid.set(group.vid, group);
    group.groupContent.forEach(g => this.addGroupToMap(g));
  }
}
