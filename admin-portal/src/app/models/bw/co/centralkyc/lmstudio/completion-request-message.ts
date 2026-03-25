import { FormBuilder } from "@angular/forms";


export class CompletionRequestMessage {
    role: string | any;
    
    content: string | any;
    
    constructor() {
        this.role = null;
        this.content = null;
    }
}
