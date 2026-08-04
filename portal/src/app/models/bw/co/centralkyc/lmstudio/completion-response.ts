
import {CompletionResponseUsage} from '@models/bw/co/kyvera/lmstudio/completion-response-usage';
import {CompetionResponseChoice} from '@models/bw/co/kyvera/lmstudio/competion-response-choice';

export class CompletionResponse {
    id: string | any;
    
    object: string | any;
    
    created: number | any;
    
    model: string | any;
    
    choices: Array<CompetionResponseChoice> | any;
    

    usage: CompletionResponseUsage | any;
    stats: any | any;
    
    systemFingerprint: string | any;
    
    constructor() {
        this.id = null;
        this.object = null;
        this.created = null;
        this.model = null;
        this.choices = [];
        this.usage = null;
        this.stats = null;
        this.systemFingerprint = null;
    }
}
