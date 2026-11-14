import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { FormDTO } from '@models/bw/co/knowvera/form/form-dto';

/** Custom forms an organisation designs for its clients to fill. */
@Service()
export class FormApi {

    protected path = '/forms';

    private http = inject(HttpClient);

    public findById(id: string): Observable<FormDTO> {

        return this.http.get<FormDTO>(`${this.path}/${id}`);
    }

    public findByOrganisation(organisationId: string): Observable<FormDTO[]> {

        return this.http.get<FormDTO[]>(`${this.path}/by-org/${organisationId}`);
    }

    public getAll(): Observable<FormDTO[]> {

        return this.http.get<FormDTO[]>(`${this.path}`);
    }

    public remove(id: string): Observable<boolean> {

        return this.http.delete<boolean>(`${this.path}/${id}`);
    }

    /** Creates the form, or updates it with all its sections and fields when it has an id. */
    public save(form: FormDTO): Observable<FormDTO> {

        return this.http.post<FormDTO>(`${this.path}`, form);
    }
}
