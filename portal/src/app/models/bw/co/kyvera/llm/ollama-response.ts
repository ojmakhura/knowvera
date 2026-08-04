
import {OllamaResponseMessage} from '@models/bw/co/kyvera/llm/ollama-response-message';

export class OllamaResponse {
    model: string | any;
    
    createdAt: Date | any;
    

    message: OllamaResponseMessage | any;
    done: boolean | any;
    
    doneReason: string | any;
    
    totalDuration: number | any;
    
    loadDuration: number | any;
    
    promptEvalCount: number | any;
    
    promptEvalDuration: number | any;
    
    evalCount: number | any;
    
    evalDuration: number | any;
    
    constructor() {
        this.model = null;
        this.createdAt = null;
        this.message = null;
        this.done = null;
        this.doneReason = null;
        this.totalDuration = null;
        this.loadDuration = null;
        this.promptEvalCount = null;
        this.promptEvalDuration = null;
        this.evalCount = null;
        this.evalDuration = null;
    }
}
