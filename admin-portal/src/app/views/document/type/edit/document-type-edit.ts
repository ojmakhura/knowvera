import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

type SectionLink = {
  id: string;
  label: string;
  icon: string;
  active?: boolean;
};

type ExpectedField = {
  name: string;
  keyField: boolean;
  mandatory: boolean;
  formats: string[];
  selectedFormat: string;
};

type PromptBlock = {
  role: string;
  content: string;
  accented?: boolean;
};

type AuditEntry = {
  title: string;
  time: string;
};

@Component({
  selector: 'app-document-type-edit',
  templateUrl: './document-type-edit.html',
  styleUrls: ['./document-type-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentTypeEdit {
  protected readonly sectionLinks = signal<SectionLink[]>([
    { id: 'general', label: 'General Details', icon: '◫', active: true },
    { id: 'fields', label: 'Expected Fields', icon: '≣' },
    { id: 'validation', label: 'AI Validation', icon: '◈' },
    { id: 'extraction', label: 'Extraction Rules', icon: '◎' },
    { id: 'permissions', label: 'Permissions', icon: '⛨' },
  ]);

  protected readonly expectedFields = signal<ExpectedField[]>([
    {
      name: 'passport_number',
      keyField: true,
      mandatory: true,
      formats: ['Alpha-Numeric', 'Date', 'Name/String'],
      selectedFormat: 'Alpha-Numeric',
    },
    {
      name: 'expiry_date',
      keyField: false,
      mandatory: true,
      formats: ['Date', 'Alpha-Numeric', 'ISO-8601'],
      selectedFormat: 'Date',
    },
    {
      name: 'full_name',
      keyField: false,
      mandatory: true,
      formats: ['Name/String', 'Upper-Case Only'],
      selectedFormat: 'Name/String',
    },
  ]);

  protected readonly validationPrompts = signal<PromptBlock[]>([
    {
      role: 'System',
      content:
        'You are a compliance officer auditing passport images. Ensure all fields are legible and no significant tampering is detected. Respond with a confidence score and a list of flags.',
    },
    {
      role: 'User',
      content:
        'Verify the expiry date against current date {{current_date}}. Flag if expired or expiring within 3 months.',
      accented: true,
    },
  ]);

  protected readonly auditEntries = signal<AuditEntry[]>([
    { title: 'Updated metadata', time: '2 minutes ago' },
    { title: 'Schema modified by Admin', time: '1 hour ago' },
  ]);
}
