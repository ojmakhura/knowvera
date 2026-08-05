
import {CompletionRequestMessage} from '@models/bw/co/knowvera/lmstudio/completion-request-message';

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
