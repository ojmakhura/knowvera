import { Injectable } from '@angular/core';
import { NativeDateAdapter } from '@angular/material/core';

// MY_DATE_FORMATS uses moment-style tokens, which NativeDateAdapter ignores: it parses with
// Date.parse (month-first, so "25/12/1990" is invalid) and passes format strings straight to Intl.
const DISPLAY_FORMATS: Record<string, Intl.DateTimeFormatOptions> = {
  'DD/MM/YYYY': { day: '2-digit', month: '2-digit', year: 'numeric' },
  'MMM YYYY': { month: 'short', year: 'numeric' },
  'MMMM YYYY': { month: 'long', year: 'numeric' },
  LL: { day: 'numeric', month: 'long', year: 'numeric' },
};

const DAY_MONTH_YEAR = /^\s*(\d{1,2})[/.-](\d{1,2})[/.-](\d{4})\s*$/;

@Injectable()
export class AppDateAdapter extends NativeDateAdapter {
  override parse(value: any, parseFormat?: any): Date | null {
    if (typeof value === 'string') {
      const match = DAY_MONTH_YEAR.exec(value);

      if (match) {
        const [, day, month, year] = match.map(Number);
        const date = new Date(year, month - 1, day);
        // Reject overflow such as 31/02/1990 instead of rolling it into March.
        const valid = date.getFullYear() === year && date.getMonth() === month - 1 && date.getDate() === day;
        return valid ? date : this.invalid();
      }
    }

    return super.parse(value, parseFormat);
  }

  override format(date: Date, displayFormat: any): string {
    const options = typeof displayFormat === 'string' ? DISPLAY_FORMATS[displayFormat] : displayFormat;
    return super.format(date, options ?? {});
  }
}
