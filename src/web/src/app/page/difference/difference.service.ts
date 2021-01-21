import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Http } from 'app/service/http.service';
import { Difference, GroupDifference } from 'app/model/difference';
import { PreloaderService } from 'app/preloader/preloader.service';
import { tap } from 'rxjs/operators';
import { TreeNode } from 'app/model/tree';
import { formatTree } from 'app/service/tree.service';
import { ActivatedRoute } from '@angular/router';
import { OperatorFunction } from 'rxjs';
import { environment } from 'environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DifferenceService {
  public difference: Difference = null;
  public differenceTree: TreeNode<GroupDifference>
  public expandMap: Map<number, boolean>

  constructor(private http: Http,
              private route: ActivatedRoute,
              private preloaderService: PreloaderService) {  }

  initDifference(cb: OperatorFunction<Difference, Difference> = tap()) {
    this.expandMap = new Map();

    let params = new HttpParams()
      .set('from', this.route.snapshot.queryParams.from.toString())
      .set('to', this.route.snapshot.queryParams.to.toString());


    console.log();

    this.preloaderService.wrap(
      this.http.get<Difference>(`${environment.backendPath}/difference`, params)
        .pipe(
          cb,
          tap(res => this.difference = res),
          tap(() => this.differenceTree = formatTree(this.difference.groupContent, (g) => g.groupContent))
        )
    )
  }
}
