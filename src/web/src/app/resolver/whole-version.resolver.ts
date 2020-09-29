import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { WholeVersion } from 'app/model/whole-version';
import { Http } from 'app/http/http.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: "root"
})
export class WholeVersionResolver implements Resolve<WholeVersion> {

  constructor(private http: Http) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<WholeVersion> {
    return this.http.get<WholeVersion>(`http://localhost:8080/version/${route.params.id}`);
  }
}
