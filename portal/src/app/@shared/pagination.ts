/** Page sizes offered by the numbered pager on the search pages. */
export const PAGE_SIZE_OPTIONS = [10, 25, 50];

/**
 * Zero-based page indexes for a numbered pager: a window of up to `size` pages centred on the
 * current page where possible.
 */
export function pageWindow(currentPage: number, totalPages: number, size = 5): number[] {
  const total = Math.max(totalPages, 1);
  const length = Math.min(size, total);
  const start = Math.min(Math.max(currentPage - Math.floor(length / 2), 0), total - length);
  return Array.from({ length }, (_, i) => start + i);
}

/** "Showing 11-20 of 42 records" for the results header. */
export function showingRecordsLabel(currentPage: number, pageSize: number, shown: number, total: number): string {
  if (!total || !shown) {
    return `Showing 0 of ${total} records`;
  }
  const start = currentPage * pageSize + 1;
  return `Showing ${start}-${start + shown - 1} of ${total} records`;
}
