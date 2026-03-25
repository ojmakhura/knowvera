import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormField } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';

type ClientRequestRow = {
  id: string;
  name: string;
  email: string;
  organisation: string;
  status: 'IN_REVIEW' | 'APPROVED' | 'REJECTED';
};

@Component({
  selector: 'app-client-requests',
  templateUrl: './client-requests.html',
  styleUrls: ['./client-requests.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    FormField,
    MatIconModule,
    MatButtonModule,
    RouterModule
  ],
})
export class ClientRequests implements OnInit, AfterViewInit, OnDestroy {
  ngOnInit(): void {}
  ngAfterViewInit(): void {}
  ngOnDestroy(): void {}

  protected readonly rows = signal<ClientRequestRow[]>([
    {
      id: 'CR-001',
      name: 'Alexander Vance Sterling',
      email: 'a.sterling@nexfix.io',
      organisation: 'Nexus Financial',
      status: 'APPROVED',
    },
    {
      id: 'CR-002',
      name: 'Sarah Drummand',
      email: 's.drummand@glc.org',
      organisation: 'Global Logistics Corp',
      status: 'IN_REVIEW',
    },
    {
      id: 'CR-003',
      name: 'Markus Thoreaux',
      email: 'm.thoreaux@aether.tech',
      organisation: 'Aether Technologies',
      status: 'REJECTED',
    },
    {
      id: 'CR-004',
      name: 'Victoria Chen',
      email: 'v.chen@primegroup.co',
      organisation: 'Prime Investment Group',
      status: 'APPROVED',
    },
  ]);

  protected readonly pages = signal([1, 2, 3]);
}
