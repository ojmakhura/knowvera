
import {LmStudioResponseUsage} from '@models/bw/co/knowvera/llm/lm-studio-response-usage';
import {LmStudioResponseChoice} from '@models/bw/co/knowvera/llm/lm-studio-response-choice';

export class LmStudioResponse {
    id: string | any;
    
    object: string | any;
    
    created: number | any;
    
    model: string | any;
    
    choices: Array<LmStudioResponseChoice> | any;
    

    usage: LmStudioResponseUsage | any;
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
