import { inject } from "@angular/core";
import { ResolveFn } from "@angular/router";
import { IndividualDTO } from "@app/models/bw/co/knowvera/individual/individual-dto";
import { IndividualApi } from "@app/services/bw/co/knowvera/individual/individual-api";

export const individualResolver: ResolveFn<IndividualDTO> = (route) => 
    inject(IndividualApi).findById(route.paramMap.get('id'))