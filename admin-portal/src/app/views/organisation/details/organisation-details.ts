import { AfterViewInit, ChangeDetectionStrategy, Component, inject, Input, linkedSignal, OnDestroy, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OrganisationApiStore } from '@app/store/bw/co/centralkyc/organisation/organisation-api.store';

@Component({
  selector: 'app-organisation-details',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './organisation-details.html',
  styleUrl: './organisation-details.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class OrganisationDetails implements OnInit, AfterViewInit, OnDestroy {

    @Input() id: string = '';
    organisationApiStore = inject(OrganisationApiStore);

    organisation = linkedSignal(() => this.organisationApiStore.data());

    constructor() {}

    ngOnInit(): void {
        console.log('OrganisationDetails ngOnInit', this.id);

        if(this.id && this.id !== '') {

            this.organisationApiStore.findById({
                id: this.id
            })
        }
    }
    
    ngAfterViewInit(): void {}

    ngOnDestroy(): void {}
}
