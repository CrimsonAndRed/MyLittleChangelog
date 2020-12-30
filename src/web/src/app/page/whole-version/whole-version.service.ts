import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { WholeVersion, WholeVersionHeader } from 'app/model/whole-version';
import { tap, switchMap, map } from 'rxjs/operators';
import { Observable, Subject } from 'rxjs';
import { GroupContent, Group, GroupToUpdate, NewGroup } from 'app/model/group-content';
import { LeafContent, LeafToUpdate, NewLeaf, NewLeafWithId } from 'app/model/leaf-content';
import { PreloaderService } from 'app/preloader/preloader.service';
import { TreeNode } from 'app/model/tree';
import { formatTree } from 'app/service/tree.service';
import { HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root',
})
export class WholeVersionService {

  public wholeVersionHeader: WholeVersionHeader = null;

  public wholeVersionTree: TreeNode<GroupContent> = null;

  public expandMap: Map<number, boolean> = new Map<number, boolean>();

  private groupsByVid: Map<number, TreeNode<GroupContent>> = new Map<number, TreeNode<GroupContent>>();

  constructor(private http: Http, private preloaderService: PreloaderService) {  }

  initWholeVersion(versionId: number): Observable<WholeVersion> {
    return this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
      .pipe(
        tap(res => this.wholeVersionHeader = res),
        tap(res => this.wholeVersionTree = formatTree(res.groupContent, (g) => g.groupContent)),
        tap(() => {
          this.groupsByVid = new Map<number, TreeNode<GroupContent>>();
          this.wholeVersionTree.children.forEach(g => this.addGroupToMap(g))
        }),
      );
  }


  createNewLeaf(newLeaf: NewLeaf, groupId: number): Observable<WholeVersion> {
    return this.http.post<NewLeafWithId>(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group/${groupId}/leaf`, newLeaf)
      .pipe(
        switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
      );
  }

  createNewGroup(newGroup: NewGroup): Observable<WholeVersion> {
    return this.http.post<Group>(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group`, newGroup)
      .pipe(
        switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
      );
  }

  updateLeaf(leafToUpdate: LeafToUpdate, groupId: number, leafId: number): Observable<WholeVersion> {
    return this.http.put<void>(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group/${groupId}/leaf/${leafId}`, leafToUpdate)
      .pipe(
        switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
      );
  }

  deleteLeaf(leafId: number, groupVid: number): Observable<void> {
    const groupId = this.groupsByVid.get(groupVid).value.id;
    return this.http.delete(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group/${groupId}/leaf/${leafId}`)
    .pipe(
      switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id)),
      map(() => {})
    )
  }

  deleteGroup(groupId: number): Observable<WholeVersion> {
    const params = new HttpParams().set('hierarchy', 'true');

    return this.http.delete(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group/${groupId}`, params)
      .pipe(
        switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
      );
  }

  dematerializeGroup(groupId: number): Observable<WholeVersion> {
    const params = new HttpParams().set('hierarchy', 'false');

    return this.http.delete(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group/${groupId}`, params)
      .pipe(
        switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
      );
  }

  updateGroup(group: GroupToUpdate, groupId: number): Observable<WholeVersion> {
    return this.http.put<void>(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group/${groupId}`, group)
      .pipe(
        switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
      )
  }

  materializeGroup(newGroup: NewGroup): Observable<void> {
    return this.http.post<Group>(`http://localhost:8080/version/${this.wholeVersionHeader.id}/group`, newGroup)
      .pipe(
        tap((group) => {
          const updateGroup = this.groupsByVid.get(group.vid).value;

          updateGroup.name = group.name;
          updateGroup.vid = group.vid;
          updateGroup.id = group.id;
          updateGroup.realNode = true;
          updateGroup.isEarliest = false;
        }),
        map(() => {})
      )
  }

  swapLeaves(parentVid: number, id1: number, id2: number): void {
    const group = this.groupsByVid.get(parentVid).value;
    const leafIdxFs = group.leafContent.findIndex(l => l.id === id1);
    const leafIdxSc = group.leafContent.findIndex(l => l.id === id2);
    const tmpLeaf = group.leafContent[leafIdxFs];
    group.leafContent[leafIdxFs] = group.leafContent[leafIdxSc];
    group.leafContent[leafIdxSc] = tmpLeaf;
  }

  swapGroups(groupVid: number, changeAgainstGroupVid: number): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersionHeader.id);
  }

  private addGroupToMap(group: TreeNode<GroupContent>): void {
    this.groupsByVid.set(group.value.vid, group);
    group.children.forEach(g => this.addGroupToMap(g));
  }
}
