// views/settings/field-groups/field-groups.ts
import { Component, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

interface KycField {
  label: string;
  hint: string;
  enabled: boolean;
}

interface FieldGroup {
  key: string;
  title: string;
  icon: string;
  status: 'Active' | 'Draft';
  fields: KycField[];
}

@Component({
  selector: 'app-field-groups',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './field-groups.html',
  styleUrls: ['./field-groups.scss'],
})
export class FieldGroups {
  activeTab = signal<'individual' | 'organisation'>('individual');

  groups = signal<FieldGroup[]>([
    {
      key: 'personal',
      title: 'Personal Data',
      icon: 'person',
      status: 'Active',
      fields: [
        { label: 'Legal Name', hint: 'First, Middle, Last Name', enabled: true },
        { label: 'Date of Birth', hint: 'Requires format validation', enabled: true },
        { label: 'National ID / Passport', hint: 'Document upload required', enabled: true },
        { label: 'Residential Address', hint: 'Proof of address needed', enabled: false },
      ],
    },
    {
      key: 'financial',
      title: 'Financial Information',
      icon: 'account_balance',
      status: 'Active',
      fields: [
        { label: 'Source of Funds', hint: 'Dropdown selection', enabled: true },
        { label: 'Estimated Annual Income', hint: 'Tiered ranges', enabled: true },
        { label: 'Tax Identification Number', hint: 'Optional based on region', enabled: false },
      ],
    },
  ]);

  toggleField(groupKey: string, field: KycField): void {
    this.groups.update((groups) =>
      groups.map((g) =>
        g.key !== groupKey
          ? g
          : { ...g, fields: g.fields.map((f) => (f === field ? { ...f, enabled: !f.enabled } : f)) },
      ),
    );
  }
}