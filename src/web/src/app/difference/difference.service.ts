import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Http } from 'app/http/http.service';
import { Difference } from 'app/model/difference';
import { Version } from 'app/model/version';
import { PreloaderService } from 'app/preloader/preloader.service';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root',
})
export class DifferenceService {
  public difference: Difference = null;

  constructor(private http: Http, private preloaderService: PreloaderService) {  }

  initDifference(fromVersion: number, toVersion: number): Observable<Difference> {

    let params = new HttpParams()
      .set('from', fromVersion.toString())
      .set('to', toVersion.toString());

    return this.http.get<Difference>('http://localhost:8080/difference', params)
      .pipe(
        tap(res => this.difference = res)
      )
  }
}
