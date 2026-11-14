/** Life cycle of a custom form: only a published form can be given to clients to fill. */
export enum FormStatus {
    DRAFT = "DRAFT",
    PUBLISHED = "PUBLISHED",
    ARCHIVED = "ARCHIVED"
}
