import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { WholeVersion, WholeVersionHeader } from 'app/model/whole-version';
import { tap, switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { GroupContent, Group, GroupToUpdate, NewGroup } from 'app/model/group-content';
import { LeafToUpdate, NewLeaf, NewLeafWithId } from 'app/model/leaf-content';
import { PreloaderService } from 'app/preloader/preloader.service';
import { TreeNode } from 'app/model/tree';
import { formatTree } from 'app/service/tree.service';
import { HttpParams } from '@angular/common/http';
import { OperatorFunction } from 'rxjs';
import { environment } from 'environments/environment';
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
    return this.http.get<WholeVersion>(`${environment.backendPath}/version/${versionId}`)
      .pipe(
        tap(res => this.wholeVersionHeader = res),
        tap(res => this.wholeVersionTree = formatTree(res.groupContent, (g) => g.groupContent)),
        tap(() => {
          this.groupsByVid = new Map<number, TreeNode<GroupContent>>();
          this.wholeVersionTree.children.forEach(g => this.addGroupToMap(g))
        }),
      );
  }


  createNewLeaf(newLeaf: NewLeaf, groupId: number, cb: OperatorFunction<NewLeafWithId, NewLeafWithId> = tap()) {
    this.preloaderService.wrap(
      this.http.post<NewLeafWithId>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${groupId}/leaf`, newLeaf)
        .pipe(
          cb,
          switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
        )
      )
  }

  createNewGroup(newGroup: NewGroup, cb: OperatorFunction<Group, Group> = tap()) {
    this.preloaderService.wrap(
      this.http.post<Group>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group`, newGroup)
        .pipe(
          cb,
          switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
        )
    );
  }

  updateLeaf(leafToUpdate: LeafToUpdate, groupId: number, leafId: number, cb: OperatorFunction<void, void> = tap()) {
    this.preloaderService.wrap(
      this.http.put<void>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${groupId}/leaf/${leafId}`, leafToUpdate)
        .pipe(
          cb,
          switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
        )
      )
  }

  deleteLeaf(leafId: number, groupVid: number, cb: OperatorFunction<void, void> = tap()) {
    const groupId = this.groupsByVid.get(groupVid).value.id;
    this.preloaderService.wrap(
     this.http.delete<void>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${groupId}/leaf/${leafId}`)
      .pipe(
        cb,
        switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
      )
    )
  }

  deleteGroup(groupId: number, cb: OperatorFunction<void, void> = tap()) {
    const params = new HttpParams().set('hierarchy', 'true');

    this.preloaderService.wrap(
      this.http.delete<void>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${groupId}`, params)
        .pipe(
          cb,
          switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
        )
    );
  }

  dematerializeGroup(groupId: number, cb: OperatorFunction<void, void> = tap()) {
    const params = new HttpParams().set('hierarchy', 'false');

    this.preloaderService.wrap(
      this.http.delete<void>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${groupId}`, params)
        .pipe(
          cb,
          switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
        )
      );
  }

  updateGroup(group: GroupToUpdate, groupId: number, cb: OperatorFunction<void, void> = tap()) {
    this.preloaderService.wrap(
      this.http.put<void>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${groupId}`, group)
        .pipe(
          cb,
          switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
        )
    );
  }

  materializeGroup(newGroup: NewGroup, cb: OperatorFunction<Group, Group> = tap()) {
    this.preloaderService.wrap(
      this.http.post<Group>(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group`, newGroup)
        .pipe(
          cb,
          tap((group) => {
            const updateGroup = this.groupsByVid.get(group.vid).value;

            updateGroup.name = group.name;
            updateGroup.vid = group.vid;
            updateGroup.id = group.id;
            updateGroup.realNode = true;
            updateGroup.isEarliest = false;
          })
        )
    );
  }

  getPrevoiusVersion(cb: OperatorFunction<WholeVersion, WholeVersion> = tap()) {
    this.preloaderService.wrap(
      this.http.get<WholeVersion>(`${environment.backendPath}/version/previous`)
        .pipe(cb)
    )
  }

  moveLeaf(leafId: number, groupVid: number, dto: any, cb: OperatorFunction<void, void> = tap()) {
    const group = this.groupsByVid.get(groupVid).value;
    this.preloaderService.wrap(
      this.http.patch(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${group.vid}/leaf/${leafId}/position`, dto)
        .pipe(
          cb,
          tap(() => {

            const leafIdxFs = group.leafContent.findIndex(l => l.id === leafId);
            const leafIdxSc = group.leafContent.findIndex(l => l.id === dto.changeAgainstId);
            const tmpLeaf = group.leafContent[leafIdxFs];
            group.leafContent[leafIdxFs] = group.leafContent[leafIdxSc];
            group.leafContent[leafIdxSc] = tmpLeaf;
          })
        )
    );
  }

  moveGroup(dto: any, groupId: number, cb: OperatorFunction<void, void> = tap()) {
    this.preloaderService.wrap(
      this.http.patch(`${environment.backendPath}/version/${this.wholeVersionHeader.id}/group/${groupId}/position`, dto)
        .pipe(
          cb,
          switchMap(() => this.initWholeVersion(this.wholeVersionHeader.id))
        )
    );
  }

  private addGroupToMap(group: TreeNode<GroupContent>) {
    this.groupsByVid.set(group.value.vid, group);
    group.children.forEach(g => this.addGroupToMap(g));
  }
}
