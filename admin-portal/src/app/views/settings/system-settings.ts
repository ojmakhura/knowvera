import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-system-settings',
  standalone: true,
  templateUrl: './system-settings.html',
  styleUrls: ['./system-settings.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SystemSettings {}
