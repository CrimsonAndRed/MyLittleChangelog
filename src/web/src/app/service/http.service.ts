import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ErrorModalComponent } from 'app/error/error-modal/error-modal.component';
import { MatDialog } from '@angular/material/dialog';

const jsonHttpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Injectable({
  providedIn: 'root'
})
export class Http {

  constructor(private httpClient: HttpClient, private dialog: MatDialog) {
  }

  get<T>(url: string, queryParams: HttpParams = new HttpParams()): Observable<T> {
    const options = {
      responseType: 'json' as const,
      params: queryParams
    }
    return this.handleErrors(this.httpClient.get<T>(url, options));
  }

  post<T>(url: string, body: any = null): Observable<T> {
    return this.handleErrors(this.httpClient.post<T>(url, body, jsonHttpOptions));
  }

  put<T>(url: string, body: any = null): Observable<T> {
    return this.handleErrors(this.httpClient.put<T>(url, body, jsonHttpOptions));
  }

  delete<T>(url: string, queryParams: HttpParams = new HttpParams()): Observable<T> {

    const options = {
      responseType: 'json' as const,
      params: queryParams
    };

    return this.handleErrors(this.httpClient.delete<T>(url, options));
  }

  handleErrors<T>(response: Observable<T>): Observable<T> {
    return response.pipe(
      tap(
        () => {},
        (resp) => {
          let messages: string[] = [];
          if (resp.status === 500) {
            messages.push("Got http code 500 PepeHands");
          } else if (resp.status === 400) {
            messages = resp.error;
          } else {
            messages.push(`Unknown status ${ resp.status }`);
          }
          const dialogRef = this.dialog.open(ErrorModalComponent, {
            hasBackdrop: true,
            minWidth: '80%',
            data: {
               errors: messages,
            }
          });
        }
      )
    );
  }

  patch<Req, Res>(url: string, body: Req): Observable<Res> {
    return this.handleErrors(this.httpClient.patch<Res>(url, body, jsonHttpOptions));
  }
}
