import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatTableModule } from '@angular/material/table';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  Input,
  linkedSignal,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { Loader } from '@app/@shared/loader/loader';
import { ToastrService } from 'ngx-toastr';
import { DocumentApiStore } from '@app/store/bw/co/centralkyc/document/document-api.store';

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
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
    MatListModule,
    MatTableModule,
    Loader,
  ],
})
export class DocumentDetails implements OnInit, AfterViewInit, OnDestroy {
  toaster: ToastrService = inject(ToastrService);
  readonly documentApiStore = inject(DocumentApiStore);

  loaderMessage = linkedSignal(() => this.documentApiStore.loaderMessage());
  messages = linkedSignal(() => this.documentApiStore.messages());
  success = linkedSignal(() => this.documentApiStore.success());
  loading = linkedSignal(() => this.documentApiStore.loading());
  error = linkedSignal(() => this.documentApiStore.error());

  @Input() id!: string;

  document = linkedSignal(() => this.documentApiStore.data());

  constructor() {
    effect(() => {
      let messages = this.messages();

      if (this.success() && !this.loading()) {
        this.toaster.success(messages[0]);
      }

      if (this.error() && !this.loading()) {
        this.toaster.error(messages[0]);
      }
    });
  }

  ngOnInit(): void {

    if(this.id) {
      this.documentApiStore.findById({id: this.id});
    }
  }

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}
}
