import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Env } from "@app/models/env.model";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EnvLoaderService {

    private http = inject(HttpClient);

    public loadEnv(): Observable<Env> {
        return this.http.get<Env>('/env.json');
    }
}