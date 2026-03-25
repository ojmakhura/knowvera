import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-client-request-edit',
  templateUrl: './client-request-edit.html',
  styleUrls: ['./client-request-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClientRequestEdit implements OnInit, AfterViewInit, OnDestroy {
  constructor() {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {}

  ngOnDestroy(): void {}
}
