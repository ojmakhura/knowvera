// Synchronised with the webservice by scripts/sync_angular_services.py. CAN EDIT
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { GroupFieldValueDTO } from '@models/bw/co/knowvera/kyc/fields/group-field-value-dto';
import { KycReportSectionDTO } from '@models/bw/co/knowvera/kyc/fields/kyc-report-section-dto';

@Injectable({
  providedIn: 'root'
})
export class KycReportSectionApi {

  protected path = '/kyc-report-sections';

  private http = inject(HttpClient);

  public addFieldValue(fieldValue: GroupFieldValueDTO | any): Observable<KycReportSectionDTO | any> {

    return this.http.post<KycReportSectionDTO | any>(`${this.path}/field-value`, fieldValue);
  }

  public findById(id: string | any): Observable<KycReportSectionDTO | any> {

    return this.http.get<KycReportSectionDTO | any>(`${this.path}/${id}`);
  }

  public remove(id: string | any): Observable<boolean | any> {

    return this.http.delete<boolean | any>(`${this.path}/${id}`);
  }

  public removeFieldValue(id: string | any, fieldValueId: string | any): Observable<KycReportSectionDTO | any> {

    return this.http.delete<KycReportSectionDTO | any>(`${this.path}/${id}/field-value/${fieldValueId}`);
  }

  public save(kycReportSection: KycReportSectionDTO | any): Observable<KycReportSectionDTO | any> {

    return this.http.post<KycReportSectionDTO | any>(`${this.path}`, kycReportSection);
  }
}
