

export class OllamaResponseMessage {
    role: string | any;
    
    content: string | any;
    
    thinking: string | any;
    
    constructor() {
        this.role = null;
        this.content = null;
        this.thinking = null;
    }
}
