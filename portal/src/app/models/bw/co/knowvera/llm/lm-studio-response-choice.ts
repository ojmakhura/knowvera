
import {PromptMessage} from '@models/bw/co/knowvera/llm/prompt-message';

export class LmStudioResponseChoice {
    index: number | any;
    
    logprobs: string | any;
    
    finish_reason: string | any;
    

    message: PromptMessage | any;
    constructor() {
        this.index = null;
        this.logprobs = null;
        this.finish_reason = null;
        this.message = null;
    }
}
