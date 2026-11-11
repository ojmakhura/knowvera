/**
 * The user's saved language. Browser storage can be missing or throw (private windows, blocked
 * site data, server-side or test environments), so a failure just means "nothing saved".
 */
const KEY = 'language';

export function readStoredLanguage(): string | null {
  try {
    return globalThis.localStorage?.getItem(KEY) ?? null;
  } catch {
    return null;
  }
}

export function storeLanguage(lang: string): void {
  try {
    globalThis.localStorage?.setItem(KEY, lang);
  } catch {
    // Not saved; the language still applies for this visit
  }
}
