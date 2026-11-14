import { Service, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { FormAssignmentDTO } from '@models/bw/co/knowvera/form/submission/form-assignment-dto';
import { FormFieldValueDTO } from '@models/bw/co/knowvera/form/submission/form-field-value-dto';
import { FormSubmissionDTO } from '@models/bw/co/knowvera/form/submission/form-submission-dto';
import { FormSubmissionStatus } from '@models/bw/co/knowvera/form/submission/form-submission-status';

/** The answers of clients to the custom forms of an organisation. */
@Service()
export class FormSubmissionApi {

    protected path = '/form-submissions';

    private http = inject(HttpClient);

    /** Adds a published form to a client's KYC record, where the client fills it. */
    public createSubmission(assignment: FormAssignmentDTO): Observable<FormSubmissionDTO> {

        return this.http.post<FormSubmissionDTO>(`${this.path}`, assignment);
    }

    /** A submission with its answers and the definition of its form. */
    public findById(id: string): Observable<FormSubmissionDTO> {

        return this.http.get<FormSubmissionDTO>(`${this.path}/${id}`);
    }

    public findByForm(formId: string): Observable<FormSubmissionDTO[]> {

        return this.http.get<FormSubmissionDTO[]>(`${this.path}/by-form/${formId}`);
    }

    /**
     * The forms that are part of a KYC record. The forms the record's client has to fill and
     * does not have yet are added first.
     */
    public findByKycRecord(kycRecordId: string): Observable<FormSubmissionDTO[]> {

        return this.http.get<FormSubmissionDTO[]>(`${this.path}/by-record/${kycRecordId}`);
    }

    public findByOrganisation(organisationId: string): Observable<FormSubmissionDTO[]> {

        return this.http.get<FormSubmissionDTO[]>(`${this.path}/by-org/${organisationId}`);
    }

    /** The forms on the KYC records of the caller and of the caller's organisation. */
    public findMySubmissions(): Observable<FormSubmissionDTO[]> {

        return this.http.get<FormSubmissionDTO[]>(`${this.path}/mine`);
    }

    public remove(id: string): Observable<boolean> {

        return this.http.delete<boolean>(`${this.path}/${id}`);
    }

    /** Saves answers without submitting them; only the given fields change. */
    public saveValues(id: string, values: FormFieldValueDTO[]): Observable<FormSubmissionDTO> {

        return this.http.post<FormSubmissionDTO>(`${this.path}/${id}/values`, values);
    }

    /** Submits the answers to the organisation; every required field must be answered. */
    public submit(id: string): Observable<FormSubmissionDTO> {

        return this.http.post<FormSubmissionDTO>(`${this.path}/${id}/submit`, null);
    }

    /** The organisation's decision on submitted answers: ACCEPTED, or RETURNED with a note. */
    public updateStatus(id: string, status: FormSubmissionStatus, note?: string): Observable<FormSubmissionDTO> {

        const params: Record<string, string> = { status };
        if (note) {
            params['note'] = note;
        }
        return this.http.post<FormSubmissionDTO>(`${this.path}/${id}/status`, null, { params });
    }
}
