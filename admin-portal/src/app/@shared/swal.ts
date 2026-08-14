let sweetAlertLoader: Promise<any> | null = null;

async function loadSweetAlert(): Promise<any> {
  if (!sweetAlertLoader) {
    sweetAlertLoader = import('sweetalert2/dist/sweetalert2.esm.all.js').then(
      (module) => module.default,
    );
  }

  return sweetAlertLoader;
}

export async function swalFire(...args: any[]): Promise<any> {
  const swal = await loadSweetAlert();
  return swal.fire(...args);
}