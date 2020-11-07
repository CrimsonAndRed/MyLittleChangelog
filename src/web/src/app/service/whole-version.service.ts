import { Injectable } from '@angular/core';
import { Http } from 'app/http/http.service';
import { WholeVersion } from 'app/model/whole-version'
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent } from 'app/model/leaf-content';

@Injectable({
  providedIn: 'root',
})
export class WholeVersionService {

  public wholeVersion: WholeVersion = null;

  private groupsByVid = {};

  constructor(private http: Http) {}

  initWholeVersion(versionId: number): Observable<WholeVersion> {
    this.wholeVersion = null;
    this.groupsByVid = {};

    return this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
      .pipe(
        tap(res => this.wholeVersion = res),
        tap(res => res.groupContent.forEach(g => this.addGroupToMap(g)))
      );
  }

  addGroupToParent(group: GroupContent, parentVid: number) {
    if (parentVid !== null) {
      this.groupsByVid[parentVid].groupContent.push(group);
    } else {
      this.wholeVersion.groupContent.push(group);
    }
    this.groupsByVid[group.vid] = group;
  }

  addLeafToParent(leaf: LeafContent, parentVid: number) {
    this.groupsByVid[parentVid].leafContent.push(leaf);
  }

  updateLeaf(leaf: LeafContent, previousParentVid: number) {
    this.groupsByVid[previousParentVid].leafContent = this.groupsByVid[previousParentVid].leafContent.filter(l => l.id !== leaf.id);
    this.groupsByVid[leaf.groupVid].leafContent.push(leaf);
  }

  // TODD(#9) Здесь происходит refresh потому что мы должны удалить все вышестоящие узлы если этот лиф был последним узлом
  // Но мы не знаем кто является родителем этого лифа, поэтому не можем этого сделать
  // Как вариант добавить parentVid в GroupContent или сделать мапу groupVid на parentVid
  deleteLeaf(leafId: number, parentVid: number) {
    this.groupsByVid[parentVid].leafContent = this.groupsByVid[parentVid].leafContent.filter(l => l.id !== leafId);
    window.location.reload();
  }

  // TODO(#9)
  deleteGroup(groupId: number, parentVid: number) {
    if (parentVid != null) {
      this.groupsByVid[parentVid].groupContent = this.groupsByVid[parentVid].groupContent.filter(g => g.id !== groupId);
      delete this.groupsByVid[parentVid];
    } else {
      this.wholeVersion.groupContent = this.wholeVersion.groupContent.filter(g => g.id !== groupId);
    }
    window.location.reload();
  }

  updateGroup(group: Group) {
    let updateGroup = this.groupsByVid[group.vid];

    updateGroup.name = group.name;
  }

  materializeGroup(group: Group) {
    let updateGroup = this.groupsByVid[group.vid];

    updateGroup.name = group.name;
    updateGroup.vid = group.vid;
    updateGroup.id = group.id;
    updateGroup.realNode = true;
    updateGroup.isEarliest = false;
  }

  // TODO(#9)
  dematerializeGroup(group: Group) {
    let updateGroup = this.groupsByVid[group.vid];

    updateGroup.name = group.name;
    updateGroup.vid = group.vid;
    updateGroup.id = group.id;
    updateGroup.realNode = false;
    window.location.reload();
  }

  private addGroupToMap(group: GroupContent) {
    this.groupsByVid[group.vid] = group;
    group.groupContent.forEach(g => this.addGroupToMap(g));
  }
}
