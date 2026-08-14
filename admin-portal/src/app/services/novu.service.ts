import { HttpClient, httpResource } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class NovuService {

    protected path = '/novu';

    private http = inject(HttpClient);

    public getNovuConfig(): Observable<any> {
        return this.http.get<any>(`${this.path}/config`);
    }

    // configs = httpResource<any>(() => `${this.path}/config`);

    loadConfigs(): Observable<any> {
        return this.http.get<any>(`${this.path}/config`);
    }
}