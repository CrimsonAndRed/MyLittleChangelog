import { Injectable } from '@angular/core';
import { Http } from 'app/http/http.service';
import { WholeVersion } from 'app/model/whole-version'
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { GroupContent, Group } from 'app/model/group-content';
import { LeafContent, UpdatedLeaf } from 'app/model/leaf-content';

@Injectable({
  providedIn: 'root',
})
export class WholeVersionService {

  public wholeVersion: WholeVersion = null;

  private groupsByVid: Map<number, GroupContent> = new Map<number, GroupContent>();

  constructor(private http: Http) {}

  initWholeVersion(versionId: number): Observable<WholeVersion> {
    this.wholeVersion = null;
    this.groupsByVid = new Map<number, GroupContent>();

    return this.http.get<WholeVersion>(`http://localhost:8080/version/${versionId}`)
      .pipe(
        tap(res => this.wholeVersion = res),
        tap(res => res.groupContent.forEach(g => this.addGroupToMap(g)))
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

  updateLeaf(updatedLeaf: UpdatedLeaf, previousParentVid: number) {
    const leaf: LeafContent = {
      id: updatedLeaf.id,
      vid: updatedLeaf.vid,
      name: updatedLeaf.name,
      valueType: updatedLeaf.valueType,
      value: updatedLeaf.value,
      groupVid: updatedLeaf.groupVid,
    };
    this.groupsByVid.get(previousParentVid).leafContent = this.groupsByVid.get(previousParentVid).leafContent.filter(l => l.id !== leaf.id);
    this.groupsByVid.get(leaf.groupVid).leafContent.push(leaf);
  }

  // TODD(#9) Здесь происходит refresh потому что мы должны удалить все вышестоящие узлы если этот лиф был последним узлом
  // Но мы не знаем кто является родителем этого лифа, поэтому не можем этого сделать
  // Как вариант добавить parentVid в GroupContent или сделать мапу groupVid на parentVid
  deleteLeaf(leafId: number, parentVid: number) {
    this.groupsByVid.get(parentVid).leafContent = this.groupsByVid.get(parentVid).leafContent.filter(l => l.id !== leafId);
    window.location.reload();
  }

  // TODO(#9)
  deleteGroup(groupId: number, parentVid: number) {
    if (parentVid != null) {
      this.groupsByVid.get(parentVid).groupContent = this.groupsByVid.get(parentVid).groupContent.filter(g => g.id !== groupId);
      this.groupsByVid.delete(parentVid);
    } else {
      this.wholeVersion.groupContent = this.wholeVersion.groupContent.filter(g => g.id !== groupId);
    }
    window.location.reload();
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

  // TODO(#9)
  dematerializeGroup(group: Group) {
    let updateGroup = this.groupsByVid.get(group.vid);

    updateGroup.name = group.name;
    updateGroup.vid = group.vid;
    updateGroup.id = group.id;
    updateGroup.realNode = false;
    window.location.reload();
  }

  private addGroupToMap(group: GroupContent) {
    this.groupsByVid.set(group.vid, group);
    group.groupContent.forEach(g => this.addGroupToMap(g));
  }
}
