
import {CompletionRequestMessage} from '@models/bw/co/kyvera/lmstudio/completion-request-message';

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
