
import {ToolSelectorDTO} from '@models/bw/co/knowvera/settings/tool-selector-dto';

export class SettingsToolSelectors {

    textExtractionTools: Array<ToolSelectorDTO> | any;
    documentConfirmationTools: Array<ToolSelectorDTO> | any;
    textProcessingTools: Array<ToolSelectorDTO> | any;
    textCleanupTools: Array<ToolSelectorDTO> | any;
    user: string | any;

    constructor() {
        this.textExtractionTools = [];
        this.documentConfirmationTools = [];
        this.textProcessingTools = [];
        this.textCleanupTools = [];
        this.user = null;
    }
}
