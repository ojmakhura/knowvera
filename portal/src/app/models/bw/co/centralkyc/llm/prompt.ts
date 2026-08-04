
import {OllamaFormat} from '@models/bw/co/kyvera/llm/ollama-format';
import {PromptMessage} from '@models/bw/co/kyvera/llm/prompt-message';

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
