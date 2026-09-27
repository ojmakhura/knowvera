import { untracked } from '@angular/core';
import { toast as sonnerToast } from 'ngx-sonner';

// ngx-sonner reads its own toast list signal while creating a toast. Called from inside an effect,
// that read subscribes the effect to the list, so adding the toast re-runs the effect, which adds
// another toast, and the page freezes. Running every call untracked keeps toasts safe in effects.
function runUntracked<F extends (...args: any[]) => any>(fn: F): F {
  return ((...args: Parameters<F>) => untracked(() => fn(...args))) as F;
}

type Toast = typeof sonnerToast;

export const toast: Toast = Object.assign(
  runUntracked(sonnerToast),
  Object.fromEntries(
    Object.entries(sonnerToast).map(([key, value]) => [key, typeof value === 'function' ? runUntracked(value) : value]),
  ),
) as Toast;
