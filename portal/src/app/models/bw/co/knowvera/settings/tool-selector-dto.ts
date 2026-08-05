
import {Tool} from '@models/bw/co/knowvera/settings/tool';

export class ToolSelectorDTO {

    id: string | any;
    tool: Tool | any;
    position: number | any;

    constructor() {
        this.id = null;
        this.tool = null;
        this.position = null;
    }
}
