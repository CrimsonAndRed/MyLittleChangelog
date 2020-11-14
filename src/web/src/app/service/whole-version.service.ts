import { Injectable } from '@angular/core';
import { Http } from 'app/http/http.service';
import { WholeVersion } from 'app/model/whole-version'
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, UpdatedLeaf } from 'app/model/leaf-content';
import { PreloaderService } from '../preloader/preloader.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class WholeVersionService {

  public wholeVersion: WholeVersion = null;

  private groupsByVid: Map<number, GroupContent> = new Map<number, GroupContent>();

  // TODO(#10) Router?
  constructor(private http: Http, private preloaderService: PreloaderService, private route: Router) {}

  initWholeVersion(versionId: number): Observable<WholeVersion> {
    this.wholeVersion = null;
    this.groupsByVid = new Map<number, GroupContent>();

    return this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
      .pipe(
        tap(res => this.wholeVersion = res),
        tap(res => res.groupContent.forEach(g => this.addGroupToMap(g))),
      );
  }

  addGroupToParent(group: GroupContent, parentVid: number) {
    if (parentVid !== null) {
      this.groupsByVid.get(parentVid).groupContent.push(group);
    } else {
      this.wholeVersion.groupContent.push(group);
    }
    this.groupsByVid.set(group.vid, group);
  }

  addLeafToParent(leaf: LeafContent, parentVid: number) {
    this.groupsByVid.get(parentVid).leafContent.push(leaf);
  }

  updateLeaf(updatedLeaf: UpdatedLeaf, previousParentVid: number): Observable<WholeVersion> {
//    const leaf: LeafContent = {
//      id: updatedLeaf.id,
//      vid: updatedLeaf.vid,
//      name: updatedLeaf.name,
//      valueType: updatedLeaf.valueType,
//      value: updatedLeaf.value,
//      groupVid: updatedLeaf.groupVid,
//    };
//    this.groupsByVid.get(previousParentVid).leafContent = this.groupsByVid.get(previousParentVid).leafContent.filter(l => l.id !== leaf.id);
//    this.groupsByVid.get(leaf.groupVid).leafContent.push(leaf);
    return this.initWholeVersion(parseInt(this.route.routerState.snapshot.root.children[0].url[1].path));
  }

  deleteLeaf(leafId: number, parentVid: number): Observable<WholeVersion> {
    return this.initWholeVersion(parseInt(this.route.routerState.snapshot.root.children[0].url[1].path));
  }

  deleteGroup(groupId: number, parentVid: number): Observable<WholeVersion> {
    return this.initWholeVersion(parseInt(this.route.routerState.snapshot.root.children[0].url[1].path));
  }

  updateGroup(group: Group) {
    let updateGroup = this.groupsByVid.get(group.vid);

    updateGroup.name = group.name;
  }

  materializeGroup(group: Group) {
    let updateGroup = this.groupsByVid.get(group.vid);

    updateGroup.name = group.name;
    updateGroup.vid = group.vid;
    updateGroup.id = group.id;
    updateGroup.realNode = true;
    updateGroup.isEarliest = false;
  }

  dematerializeGroup(group: Group): Observable<WholeVersion> {
    return this.initWholeVersion(parseInt(this.route.routerState.snapshot.root.children[0].url[1].path));
  }

  private addGroupToMap(group: GroupContent) {
    this.groupsByVid.set(group.vid, group);
    group.groupContent.forEach(g => this.addGroupToMap(g));
  }
}
