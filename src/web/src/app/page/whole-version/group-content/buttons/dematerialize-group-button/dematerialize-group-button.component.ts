import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Http } from 'app/service/http.service';
import { GroupContent, Group, NewGroup } from 'app/model/group-content';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { WholeVersionService } from 'app/page/whole-version/whole-version.service';
import { TreeNode } from 'app/model/tree';
import { WholeVersion } from 'app/model/whole-version';

@Component({
  selector: 'dematerialize-group-button',
  templateUrl: './dematerialize-group-button.component.html',
  styleUrls: ['./dematerialize-group-button.component.scss']
})
export class DematerializeGroupButtonComponent {

  @Input() node: TreeNode<GroupContent>;
  @Output() onDematerializeGroup = new EventEmitter<number>();

  constructor(private http: Http, private route: ActivatedRoute, private wholeVersionService: WholeVersionService) {}

  onDematerializeClick(): void {
    this.onDematerializeGroup.emit(this.node.value.id);
  }
}
