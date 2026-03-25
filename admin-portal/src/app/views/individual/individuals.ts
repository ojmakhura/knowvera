import { CommonModule } from '@angular/common';
import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormField } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from "@angular/router";

type IndividualRow = {
  initials: string;
  name: string;
  identityNo: string;
  email: string;
  status: 'VERIFIED' | 'PENDING' | 'FLAGGED';
};

@Component({
  selector: 'app-individuals',
  templateUrl: './individuals.html',
  styleUrls: ['./individuals.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, MatIconModule, MatButtonModule, FormField, RouterLink],
})
export class Individuals implements OnInit, AfterViewInit, OnDestroy {
  ngOnInit(): void {
  }
  ngAfterViewInit(): void {
  }
  ngOnDestroy(): void {
  }
  protected readonly rows = signal<IndividualRow[]>([
    {
      initials: 'JS',
      name: 'Julianne Sterling',
      identityNo: 'VP-4420-112',
      email: 'j.sterling@veritas.io',
      status: 'VERIFIED',
    },
    {
      initials: 'MC',
      name: 'Marcus Chen',
      identityNo: 'VP-8821-990',
      email: 'marcus.c@globalreach.com',
      status: 'PENDING',
    },
    {
      initials: 'ER',
      name: 'Elena Rodriguez',
      identityNo: 'VP-1004-332',
      email: 'elena_r@secure-mail.net',
      status: 'FLAGGED',
    },
    {
      initials: 'AK',
      name: 'Alistair Kane',
      identityNo: 'VP-2256-887',
      email: 'a.kane@corporate.uk',
      status: 'VERIFIED',
    },
  ]);

  protected readonly pages = signal([1, 2, 3]);
}
