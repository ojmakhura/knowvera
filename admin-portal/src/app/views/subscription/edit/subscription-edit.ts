import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-subscription-edit',
  templateUrl: './subscription-edit.html',
  styleUrls: ['./subscription-edit.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SubscriptionEdit {}
