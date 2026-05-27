import type { SweetAlertIcon, SweetAlertOptions, SweetAlertResult } from 'sweetalert2';

type SwalTextArgs = [title: string, html?: string, icon?: SweetAlertIcon];
type SwalObjectArgs = [options: SweetAlertOptions];
type SwalArgs = SwalTextArgs | SwalObjectArgs;

export async function swalFire(...args: SwalArgs): Promise<SweetAlertResult<unknown>> {
  const { default: Swal } = await import('sweetalert2');

  if (typeof args[0] === 'string') {
    const [title, html, icon] = args;
    return Swal.fire(title, html, icon);
  }

  return Swal.fire(args[0]);
}
