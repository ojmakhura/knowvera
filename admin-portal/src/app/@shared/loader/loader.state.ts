import { Injectable, signal } from "@angular/core";

export type LoaderType = 'spinner' | 'dots' | 'pulse' | 'bars';
export type LoaderColor = 'primary' | 'accent' | 'warn' | 'white';

@Injectable({
  providedIn: 'root',
})
export class LoaderState {

  isLoading = signal(false);
  type = signal<LoaderType>('spinner');
  size = signal<'small' | 'medium' | 'large'>('medium');
  color = signal<LoaderColor>('primary');
  message = signal<string | undefined>(undefined);
  overlay = signal(true);
  fullScreen = signal(true);
}
