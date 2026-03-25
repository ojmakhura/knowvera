import { FormBuilder } from "@angular/forms";

import {CompletionRequestMessage} from '@models/bw/co/centralkyc/lmstudio/completion-request-message';

export class CompetionResponseChoice {
    index: number | any;
    
    logprobs: string | any;
    
    finish_reason: string | any;
    

    message: CompletionRequestMessage | any;
    constructor() {
        this.index = null;
        this.logprobs = null;
        this.finish_reason = null;
        this.message = null;
    }
}
