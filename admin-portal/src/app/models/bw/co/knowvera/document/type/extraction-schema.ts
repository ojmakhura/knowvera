/** A field veritas-processor extracts. */
export class ExtractionSchemaField {
    name: string | any = null;
    label: string | any = null;
    type: string | any = null;
    format: string | any = null;
    many: boolean = false;
    mandatory: boolean = false;
}

/** The veritas-processor schema that holds the fields extracted from a document type's documents. */
export class ExtractionSchema {
    code: string | any = null;
    version: number | any = null;
    name: string | any = null;
    description: string | any = null;
    fields: ExtractionSchemaField[] = [];
}
