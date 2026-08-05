
import {CompletionRequestMessage} from '@models/bw/co/knowvera/lmstudio/completion-request-message';

export class CompletionRequest {
    model: string | any;
    
    stream: boolean | any = false;
    
    messages: Array<CompletionRequestMessage> | any;
    
    constructor() {
        this.model = null;
        this.stream = false;
        this.messages = [];
    }
}
