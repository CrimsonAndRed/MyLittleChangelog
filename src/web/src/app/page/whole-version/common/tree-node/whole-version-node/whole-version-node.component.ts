import { Component, Input } from '@angular/core';
import { GroupHeader, GroupHeaderData, GroupsSecContext } from 'app/groups-sec/groups-sec.model';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Http } from 'app/service/http.service';
import { TreeNode } from 'app/model/tree';

@Component({
  selector: 'whole-version-node',
  templateUrl: './whole-version-node.component.html',
  styleUrls: ['./whole-version-node.component.scss']
})
export class WholeVersionNodeComponent<T> implements GroupHeader {

  data: GroupHeaderData;
  ctx: GroupsSecContext;

  @Input() node: TreeNode<T>;


  constructor(private wholeVersionService: WholeVersionService,
              private preloaderService: PreloaderService,
              private http: Http) { }
}
