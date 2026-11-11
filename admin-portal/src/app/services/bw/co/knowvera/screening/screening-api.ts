// Synchronised with the webservice by scripts/sync_angular_services.py. CAN EDIT
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ScreeningApi {

  protected path = '/screening';

  private http = inject(HttpClient);

  public findHitsByRecord(kycRecordId: string | any): Observable<any[] | any> {

    return this.http.get<any[] | any>(`${this.path}/records/${kycRecordId}/hits`);
  }

  public findPendingHits(): Observable<any[] | any> {

    return this.http.get<any[] | any>(`${this.path}/hits/pending`);
  }

  public getListStatus(): Observable<any[] | any> {

    return this.http.get<any[] | any>(`${this.path}/lists`);
  }

  public reviewHit(id: string | any, status: any, notes: string | any): Observable<any> {

    // Free text: sent as encoded parameters
    const params = new HttpParams().set('status', status).set('notes', notes ?? '');
    return this.http.post<any>(`${this.path}/hits/${id}/review`, null, { params });
  }

  public screenRecord(kycRecordId: string | any): Observable<any[] | any> {

    return this.http.post<any[] | any>(`${this.path}/records/${kycRecordId}/screen`, null);
  }

  public updateSanctionsList(): Observable<any> {

    return this.http.post<any>(`${this.path}/lists/update`, null);
  }
}
