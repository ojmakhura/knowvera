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
