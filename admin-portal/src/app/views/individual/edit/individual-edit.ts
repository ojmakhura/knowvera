import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';

@Component({
  selector: 'app-individual-edit',
  templateUrl: './individual-edit.html',
  styleUrls: ['./individual-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class IndividualEdit implements OnInit, AfterViewInit, OnDestroy {
    constructor() {}

    ngOnInit(): void {}

    ngAfterViewInit(): void {}

    ngOnDestroy(): void {}
}
