import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

const jsonHttpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class Http {

  constructor(private httpClient: HttpClient) {
  }

  get<T>(url: string): Observable<T> {
    return this.httpClient.get<T>(url);
  }

  post<T>(url: string, body: any = null): Observable<T> {
      return this.httpClient.post<T>(url, body, jsonHttpOptions);
  }

  put<T>(url: string, body: any = null): Observable<T> {
      return this.httpClient.put<T>(url, body, jsonHttpOptions);
  }

  delete<T>(url: string, queryParams: HttpParams = new HttpParams()): Observable<T> {

    const options = {
      responseType: 'json' as const,
      params: queryParams
    }

    return this.httpClient.delete<T>(url, options);
  }
}
