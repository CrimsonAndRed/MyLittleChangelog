import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ErrorModalComponent } from 'app/error/error-modal/error-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class Http {

  constructor(private httpClient: HttpClient, private dialog: MatDialog, private router: Router) {
  }

  get<T>(url: string, queryParams: HttpParams = new HttpParams()): Observable<T> {
    const options = {
      responseType: 'json' as const,
      params: queryParams,
      headers: new HttpHeaders({"Authorization": `Bearer ${localStorage.getItem("token")}`})
    }
    return this.handleErrors(this.httpClient.get<T>(url, options));
  }

  post<T>(url: string, body: any = null): Observable<T> {
    const options = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': `Bearer ${localStorage.getItem("token")}`
      })
    };
    return this.handleErrors(this.httpClient.post<T>(url, body, options));
  }

  put<T>(url: string, body: any = null): Observable<T> {
    return this.handleErrors(this.httpClient.put<T>(url, body, this.produceOptions()));
  }

  delete<T>(url: string, queryParams: HttpParams = new HttpParams()): Observable<T> {

    const options = {
      responseType: 'json' as const,
      params: queryParams,
      headers: new HttpHeaders({"Authorization": `Bearer ${localStorage.getItem("token")}`})
    };

    return this.handleErrors(this.httpClient.delete<T>(url, options));
  }

  patch<Req, Res>(url: string, body: Req): Observable<Res> {
    return this.handleErrors(this.httpClient.patch<Res>(url, body, this.produceOptions()));
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
          } else if (resp.status === 401) {
            this.router.navigate(['/'], {skipLocationChange: false})
            return
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

  produceOptions() {
    return {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': `Bearer ${localStorage.getItem("token")}`
      })
    };
  }
}
