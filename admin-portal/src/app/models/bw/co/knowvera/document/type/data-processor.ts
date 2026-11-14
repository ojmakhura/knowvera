/** A field a data processor expects in the data: its definition (matching it to a client is knowvera's). */
export class DataProcessorField {
    id: string | any = null;
    field: string | any = null;
    fieldLabel: string | any = null;
    fieldType: string | any = null;
    format: string | any = null;
    many: boolean = false;
    mandatory: boolean = false;
}

/**
 * A data processor of veritas-processor: what is done with the documents of the document types
 * linked to it, and the fields expected in them.
 */
export class DataProcessor {
    id: string | any = null;
    code: string | any = null;
    name: string | any = null;
    description: string | any = null;
    expectedFields: DataProcessorField[] = [];
}
