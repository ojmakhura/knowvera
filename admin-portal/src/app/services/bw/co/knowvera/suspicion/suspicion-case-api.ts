// Synchronised with the webservice by scripts/sync_angular_services.py. CAN EDIT
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SuspicionCaseApi {

  protected path = '/suspicion-cases';

  private http = inject(HttpClient);

  public addNote(id: string | any, note: string | any): Observable<any> {

    return this.http.post<any>(`${this.path}/${id}/notes`, null, { params: new HttpParams().set('note', note ?? '') });
  }

  public close(id: string | any, outcome: string | any): Observable<any> {

    return this.http.post<any>(`${this.path}/${id}/close`, null, { params: new HttpParams().set('outcome', outcome ?? '') });
  }

  public draftReport(id: string | any): Observable<string | any> {

    return this.http.get(`${this.path}/${id}/draft-report`, { responseType: 'text' });
  }

  public escalate(id: string | any): Observable<any> {

    return this.http.post<any>(`${this.path}/${id}/escalate`, null);
  }

  public findActive(): Observable<any[] | any> {

    return this.http.get<any[] | any>(`${this.path}/active`);
  }

  public findById(id: string | any): Observable<any> {

    return this.http.get<any>(`${this.path}/${id}`);
  }

  public findByRecord(kycRecordId: string | any): Observable<any[] | any> {

    return this.http.get<any[] | any>(`${this.path}/by-record/${kycRecordId}`);
  }

  public raise(suspicionCase: any): Observable<any> {

    return this.http.post<any>(`${this.path}`, suspicionCase);
  }

  public recordReport(id: string | any, fiaReference: string | any): Observable<any> {

    return this.http.post<any>(`${this.path}/${id}/report`, null, { params: new HttpParams().set('fiaReference', fiaReference ?? '') });
  }
}
