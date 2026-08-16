// views/settings/template-mappings/template-mappings.ts
import { Component, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

interface TemplateConfig {
  key: string;
  title: string;
  description: string;
  icon: string;
  status: 'Active' | 'Draft';
  currentTemplate: string;
  lastUpdated: string;
  documentType: string;
  documentTypeOptions: string[];
  mappedType: string;
  mappedTypeOptions: string[];
  confidenceThreshold: number;
  anomalyTolerance: number;
  acceptedFormats: string;
}

@Component({
  selector: 'app-template-mappings',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './template-mappings.html',
  styleUrls: ['./template-mappings.scss'],
})
export class TemplateMappings {
  configs = signal<TemplateConfig[]>([
    {
      key: 'invoice',
      title: 'Invoice Configuration',
      description: 'Map and manage invoice document structures.',
      icon: 'receipt_long',
      status: 'Active',
      currentTemplate: 'INV-STD-v2.1',
      lastUpdated: 'Oct 24, 2023 by Admin',
      documentType: 'Standard Commercial Invoice',
      documentTypeOptions: ['Standard Commercial Invoice', 'Pro Forma Invoice', 'Tax Invoice'],
      mappedType: 'JSON Standard (v2)',
      mappedTypeOptions: ['JSON Standard (v2)', 'XML Legacy (v1.5)'],
      confidenceThreshold: 95,
      anomalyTolerance: 2,
      acceptedFormats: '.json, .xml (Max 5MB)',
    },
    {
      key: 'quotation',
      title: 'Quotation Configuration',
      description: 'Define mapping structures for quote extraction.',
      icon: 'request_quote',
      status: 'Draft',
      currentTemplate: 'QT-BETA-v1.0',
      lastUpdated: 'Oct 10, 2023 by System',
      documentType: 'Enterprise Quote',
      documentTypeOptions: ['Enterprise Quote', 'SMB Estimate'],
      mappedType: 'JSON Standard (v2)',
      mappedTypeOptions: ['JSON Standard (v2)', 'JSON Strict (v3)'],
      confidenceThreshold: 88,
      anomalyTolerance: 5,
      acceptedFormats: '.json (Max 5MB)',
    },
  ]);

  updateField<K extends keyof TemplateConfig>(key: string, field: K, value: TemplateConfig[K]): void {
    this.configs.update((list) =>
      list.map((c) => (c.key === key ? { ...c, [field]: value } : c)),
    );
  }

  save(): void {
    console.log('Saving template mappings', this.configs());
  }
}