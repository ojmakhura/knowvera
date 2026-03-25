import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormField } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from "@angular/router";

type DocumentRow = {
  id: string;
  fileName: string;
  documentType: string;
  target: string;
  submittedBy: string;
  status: 'VERIFIED' | 'PENDING' | 'FLAGGED';
};

@Component({
  selector: 'app-documents',
  templateUrl: './documents.html',
  styleUrls: ['./documents.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, MatButtonModule, FormField, RouterLink],
})
export class Documents implements OnInit, AfterViewInit, OnDestroy {
  ngOnInit(): void {}
  ngAfterViewInit(): void {}
  ngOnDestroy(): void {}

  protected readonly rows = signal<DocumentRow[]>([
    {
      id: 'DOC-001',
      fileName: 'KYC_Passport_Smith_J.pdf',
      documentType: 'Identity Verification',
      target: 'Johnathan Smith',
      submittedBy: 'Compliance Desk',
      status: 'VERIFIED',
    },
    {
      id: 'DOC-002',
      fileName: 'Utility_Bill_May_2023.pdf',
      documentType: 'Proof of Residence',
      target: 'Alpha Corp Ltd',
      submittedBy: 'Client Services',
      status: 'PENDING',
    },
    {
      id: 'DOC-003',
      fileName: 'Incorporation_Cert_Final.pdf',
      documentType: 'Articles of Association',
      target: 'Global Ventures LLC',
      submittedBy: 'External Counsel',
      status: 'FLAGGED',
    },
    {
      id: 'DOC-004',
      fileName: 'Bank_Stmt_Q1_2023.pdf',
      documentType: 'Financial Statement',
      target: 'Sarah Jenkins',
      submittedBy: 'Operations Team',
      status: 'VERIFIED',
    },
  ]);

  protected readonly pages = signal([1, 2, 3]);
}
