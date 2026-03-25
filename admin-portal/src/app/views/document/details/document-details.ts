import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

type DataPoint = {
  label: string;
  value: string;
};

type CoverageItem = {
  field: string;
  confidence: string;
};

type IntegritySignal = {
  label: string;
  value: string;
};

@Component({
  selector: 'app-document-details',
  templateUrl: './document-details.html',
  styleUrls: ['./document-details.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DocumentDetails {
  protected readonly dataPoints = signal<DataPoint[]>([
    { label: 'Full Name', value: 'JOHNATHAN DOE' },
    { label: 'Document Number', value: 'EP7882104' },
    { label: 'Date of Birth', value: '14 JAN 1988' },
    { label: 'Expiration Date', value: '14 JAN 2028' },
    { label: 'Issuing Authority', value: 'IPSWICH OFFICE' },
    { label: 'Nationality', value: 'BRITISH CITIZEN' },
  ]);

  protected readonly coverage = signal<CoverageItem[]>([
    { field: 'mrz_code', confidence: '0.992' },
    { field: 'biometric_photo', confidence: '0.978' },
    { field: 'place_of_birth', confidence: '0.954' },
    { field: 'signature_scan', confidence: '0.891' },
  ]);

  protected readonly integritySignals = signal<IntegritySignal[]>([
    { label: 'Metadata Consistency', value: 'High' },
    { label: 'OCR Clarity', value: 'Optimal' },
    { label: 'Anti-Tamper Check', value: 'Pass' },
  ]);

  protected readonly confidenceSegments = signal([true, true, true, true, false]);
}
