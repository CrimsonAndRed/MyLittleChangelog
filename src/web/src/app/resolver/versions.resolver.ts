import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { Version } from 'app/model/version';
import { Http } from 'app/http/http.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: "root"
})
export class VersionsResolver implements Resolve<Version[]> {

  constructor(private http: Http) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Version[]> {
    return this.http.get<Version[]>('http://localhost:8080/version');
  }
}
