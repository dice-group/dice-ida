import {EventEmitter, Injectable} from '@angular/core';
import {HttpClient, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpParams, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RestService implements HttpInterceptor {
  private hosturl = environment.apiBase;
  public requestEvnt: EventEmitter<boolean> = new EventEmitter();

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // Clone the request and replace the original headers with
    // cloned headers, updated with the authorization.
    const authReq = req.clone({
      withCredentials: true
      /*headers: req.headers.set('Content-Type', 'application/json')*/
    });
    // send cloned request with header to the next handler.
    const reqObs: Observable<HttpEvent<any>> = next.handle(authReq);
    return reqObs;
  }

  constructor(private http: HttpClient) {
  }

  getFullUrl(path: string) {
    return this.hosturl + path;
  }

  public getNPRequest(path: string): Observable<any> {
    return this.http.get(this.getFullUrl(path));
  }

  public getRequest(path: string, prmobj: object): Observable<any> {
    let params = new HttpParams();
    for (const x in prmobj) {
      if (prmobj[x] != null) {
        params = params.set(x, prmobj[x]);
      }
    }
    this.requestEvnt.emit(true);
    const reqObs = this.http.get(this.getFullUrl(path), {params: params});
    return reqObs;
  }

  public postRequest(path: string, body: object,  prmobj: object): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': path === '/auth/login-action' ? 'application/x-www-form-urlencoded' : 'application/json'
    });
    const options = {headers: headers};
    const reqObs = this.http.post(this.getFullUrl(path), path === '/auth/login-action' ? this.getFormUrlEncoded(body) : body, options);
    return reqObs;
  }

  public deleteRequest(path: string): Observable<any> {
    this.requestEvnt.emit(true);
    // const headers = new HttpHeaders({
    //   'Content-Type': 'application/json',
    // });
    const options = {};
    const reqObs = this.http.delete(this.getFullUrl(path), options);
    return reqObs;
  }

  public getFormUrlEncoded(toConvert) {
    const formBody = [];
    for(const property in toConvert) {
      const encodedKey = encodeURIComponent(property);
      const encodedValue = encodeURIComponent(toConvert[property]);
      formBody.push(encodedKey + '=' + encodedValue);
    }
    return formBody.join('&');
  }
}
