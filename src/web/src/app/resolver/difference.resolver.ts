import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/http/http.service';
import { Observable } from 'rxjs';
import { Difference } from 'app/model/difference';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DifferenceResolver implements Resolve<Difference> {

  constructor(private http: Http) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Difference> {
    let params = new HttpParams()
      .set('from', route.queryParams.from)
      .set('to', route.queryParams.to);

    return this.http.get<Difference>('http://localhost:8080/difference', params);
  }
}
