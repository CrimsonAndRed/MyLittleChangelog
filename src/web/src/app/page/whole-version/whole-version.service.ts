import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { WholeVersion, WholeVersionHeader } from 'app/model/whole-version';
import { tap } from 'rxjs/operators';
import { Observable, Subject } from 'rxjs';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';
import { PreloaderService } from 'app/preloader/preloader.service';
import { TreeNode } from 'app/model/tree';
import { formatTree } from 'app/service/tree.service';
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

  addLeafToParent(leaf: LeafContent, parent: TreeNode<GroupContent>): void {
    parent.value.leafContent.push(leaf);
  }

  createNewGroup(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersionHeader.id);
  }

  updateLeaf(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersionHeader.id);
  }

  deleteLeaf(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersionHeader.id);
  }

  deleteGroup(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersionHeader.id);
  }

  updateGroup(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersionHeader.id);
  }

  materializeGroup(group: Group): void {
    const updateGroup = this.groupsByVid.get(group.vid).value;

    updateGroup.name = group.name;
    updateGroup.vid = group.vid;
    updateGroup.id = group.id;
    updateGroup.realNode = true;
    updateGroup.isEarliest = false;
  }

  dematerializeGroup(): Observable<WholeVersion> {
    return this.initWholeVersion(this.wholeVersionHeader.id);
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
