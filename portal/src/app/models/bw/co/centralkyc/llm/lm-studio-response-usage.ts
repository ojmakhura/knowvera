

export class LmStudioResponseUsage {
    promptTokens: number | any;
    
    completionTokens: number | any;
    
    totalTokens: number | any;
    
    constructor() {
        this.promptTokens = null;
        this.completionTokens = null;
        this.totalTokens = null;
    }
}
