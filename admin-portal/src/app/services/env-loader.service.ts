import { HttpClient } from "@angular/common/http";
import { inject, Service } from "@angular/core";
import { Env } from "@app/models/env.model";
import { Observable } from "rxjs";

@Service()
export class EnvLoaderService {

    private http = inject(HttpClient);

    public loadEnv(): Observable<Env> {
        return this.http.get<Env>('/env.json');
    }
}