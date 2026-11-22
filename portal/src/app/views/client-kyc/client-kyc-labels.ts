/** "DOCUMENT_VERIFICATION_FAILED" → "Document verification failed". */
export function labelOf(value: string | null | undefined): string {
  if (!value) {
    return '—';
  }
  const words = String(value).toLowerCase().replace(/_/g, ' ');
  return words.charAt(0).toUpperCase() + words.slice(1);
}

/** The chip tone for a KYC compliance status. */
export function complianceTone(status: string | null | undefined): string {
  switch (status) {
    case 'CURRENT':
      return 'kv-tone-success';
    case 'PENDING_APPROVAL':
    case 'INCOMPLETE':
      return 'kv-tone-warning';
    case 'DOCUMENT_VERIFICATION_FAILED':
    case 'EXPIRED':
      return 'kv-tone-danger';
    default:
      return 'kv-tone-neutral';
  }
}

/** The chip tone for a risk level. */
export function riskTone(level: string | null | undefined): string {
  switch (level) {
    case 'LOW':
      return 'kv-tone-success';
    case 'MEDIUM':
      return 'kv-tone-warning';
    case 'HIGH':
      return 'kv-tone-danger';
    default:
      return 'kv-tone-neutral';
  }
}

/** The `dp-pill` tone (is-success, is-warning, …) for a KYC compliance status. */
export function pillTone(status: string | null | undefined): string {
  return complianceTone(status).replace('kv-tone-', 'is-');
}

/** The icon shown in a KYC compliance status pill. */
export function pillIcon(status: string | null | undefined): string {
  switch (pillTone(status)) {
    case 'is-success':
      return 'check_circle';
    case 'is-warning':
      return 'schedule';
    case 'is-danger':
      return 'error';
    default:
      return 'help';
  }
}

/** "Thato Mokoena" → "TM". */
export function initials(name: string | null | undefined): string {
  return (
    (name || '')
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map((part) => part.charAt(0).toUpperCase())
      .join('') || '—'
  );
}
