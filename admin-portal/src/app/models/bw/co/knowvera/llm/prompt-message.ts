

export class PromptMessage {
    role: string | any;
    
    content: string | any;
    
    images: Array<string> | any;
    
    constructor() {
        this.role = null;
        this.content = null;
        this.images = [];
    }
}
