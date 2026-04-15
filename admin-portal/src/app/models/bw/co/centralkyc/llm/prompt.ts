
import {PromptMessage} from '@models/bw/co/centralkyc/llm/prompt-message';
import {OllamaFormat} from '@models/bw/co/centralkyc/llm/ollama-format';

export class Prompt {
    model: string | any;
    
    stream: boolean | any = false;
    
    messages: Array<PromptMessage> | any;
    

    format: OllamaFormat | any;
    constructor() {
        this.model = null;
        this.stream = false;
        this.messages = [];
        this.format = null;
    }
}
