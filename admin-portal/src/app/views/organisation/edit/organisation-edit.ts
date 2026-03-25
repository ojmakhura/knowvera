import { AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-organisation-edit',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './organisation-edit.html',
  styleUrl: './organisation-edit.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OrganisationEdit implements OnInit, AfterViewInit, OnDestroy {

    constructor() {}

    ngOnInit(): void {}

    ngAfterViewInit(): void {}

    ngOnDestroy(): void {}
}
