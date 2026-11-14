/** The input a form field is captured with. */
export enum FormFieldType {
    TEXT = "TEXT",
    TEXTAREA = "TEXTAREA",
    NUMBER = "NUMBER",
    DATE = "DATE",
    DATETIME = "DATETIME",
    TIME = "TIME",
    EMAIL = "EMAIL",
    TEL = "TEL",
    URL = "URL",
    CHECKBOX = "CHECKBOX",
    SELECT = "SELECT",
    RADIO = "RADIO",
    MULTI_SELECT = "MULTI_SELECT"
}

/** The field types whose value comes from the field's options. */
export const FORM_FIELD_TYPES_WITH_OPTIONS = [
    FormFieldType.SELECT,
    FormFieldType.RADIO,
    FormFieldType.MULTI_SELECT,
];
