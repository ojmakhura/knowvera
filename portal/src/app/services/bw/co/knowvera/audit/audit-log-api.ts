// Synchronised with the webservice by scripts/sync_angular_services.py. CAN EDIT
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { AuditLogCriteria } from '@models/bw/co/knowvera/audit/audit-log-criteria';
import { AuditLogDTO } from '@models/bw/co/knowvera/audit/audit-log-dto';
import { Page } from '@models/page.model';

@Injectable({
  providedIn: 'root'
})
export class AuditLogApi {

  protected path = '/audit-logs';

  private http = inject(HttpClient);

  public findById(id: string | any): Observable<AuditLogDTO | any> {

    return this.http.get<AuditLogDTO | any>(`${this.path}/${id}`);
  }

  public getAll(): Observable<AuditLogDTO[] | any> {

    return this.http.get<AuditLogDTO[] | any>(`${this.path}`);
  }

  public getAllPaged(pageNumber: number | any, pageSize: number | any): Observable<Page<AuditLogDTO> | any> {

    return this.http.get<Page<AuditLogDTO> | any>(`${this.path}/paged?pageNumber=${pageNumber}&pageSize=${pageSize}`);
  }

  public pagedSearch(criteria: AuditLogCriteria | any, pageNumber: number | any, pageSize: number | any): Observable<Page<AuditLogDTO> | any> {

    return this.http.post<Page<AuditLogDTO> | any>(`${this.path}/search/paged?pageNumber=${pageNumber}&pageSize=${pageSize}`, criteria);
  }

  public remove(id: string | any): Observable<boolean | any> {

    return this.http.delete<boolean | any>(`${this.path}/${id}`);
  }

  public save(auditLog: AuditLogDTO | any): Observable<AuditLogDTO | any> {

    return this.http.post<AuditLogDTO | any>(`${this.path}`, auditLog);
  }

  public search(criteria: AuditLogCriteria | any): Observable<AuditLogDTO[] | any> {

    return this.http.post<AuditLogDTO[] | any>(`${this.path}/search`, criteria);
  }
}
